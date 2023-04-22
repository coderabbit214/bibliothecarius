package com.coderabbit214.bibliothecarius.document;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.dataset.Dataset;
import com.coderabbit214.bibliothecarius.dataset.DatasetService;
import com.coderabbit214.bibliothecarius.document.aliparser.AliParserService;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrant;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrantService;
import com.coderabbit214.bibliothecarius.document.file.FileFactory;
import com.coderabbit214.bibliothecarius.document.file.FileInterface;
import com.coderabbit214.bibliothecarius.qdrant.QdrantService;
import com.coderabbit214.bibliothecarius.qdrant.point.Payload;
import com.coderabbit214.bibliothecarius.qdrant.point.PayloadTypeEnum;
import com.coderabbit214.bibliothecarius.qdrant.point.Point;
import com.coderabbit214.bibliothecarius.qdrant.point.PointCreateRequest;
import com.coderabbit214.bibliothecarius.storage.S3Service;
import com.coderabbit214.bibliothecarius.storage.StorageUtils;
import com.coderabbit214.bibliothecarius.vector.VectorFactory;
import com.coderabbit214.bibliothecarius.vector.VectorInterface;
import com.coderabbit214.bibliothecarius.vector.VectorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * <p>
 * 文件数据 服务实现类
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Service
@Slf4j
public class DocumentService extends ServiceImpl<DocumentMapper, Document> {
    private final VectorFactory vectorFactory;

    private final DatasetService datasetService;

    private final DocumentService documentService;

    private final DocumentQdrantService documentQdrantService;

    private final FileFactory fileFactory;

    private final S3Service s3Service;

    public DocumentService(VectorFactory vectorFactory, @Lazy DatasetService datasetService, @Lazy DocumentService documentService, DocumentQdrantService documentQdrantService, S3Service s3Service, @Lazy FileFactory fileFactory) {
        this.vectorFactory = vectorFactory;
        this.datasetService = datasetService;
        this.documentService = documentService;
        this.documentQdrantService = documentQdrantService;
        this.s3Service = s3Service;
        this.fileFactory = fileFactory;
    }

    /**
     * 文件数据上传
     *
     * @param name
     * @param files
     */
    public void uploadFile(String name, MultipartFile[] files, List<String> tags) {
        Dataset dataset = datasetService.getByName(name);
        if (dataset == null) {
            throw new BusinessException("dataset does not exist");
        }
        documentService.add(dataset, files, dataset.getVectorType(), tags);
    }

    /**
     * 文件上传
     *
     * @param dataset
     * @param files
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Dataset dataset, MultipartFile[] files, String vectorType, List<String> tags) {
        //判读文件类型
        String type;
        String fileHash;
        try {
            //根据后缀读取文件类型
            for (MultipartFile file : files) {
                String[] nameSplit = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                type = nameSplit[nameSplit.length - 1];
                fileHash = DigestUtils.md5Hex(file.getInputStream());

                if (!FileInterface.SUPPORT_FILE_TYPE.contains(type)) {
                    throw new BusinessException("unsupported file type");
                }
                String fileKey;
                //检查hash在数据库中是否存在
                List<Document> byHash = this.getByHash(fileHash);
                if (byHash.size() > 0) {
                    fileKey = byHash.get(0).getFileKey();
                    for (Document document : byHash) {
                        if (Objects.equals(document.getDatasetId(), dataset.getId())) {
                            throw new BusinessException("File already exists");
                        }
                    }
                } else {
                    fileKey = StorageUtils.getObjectName(file.getOriginalFilename());
                    s3Service.putObject(StorageUtils.BUCKET_NAME, fileKey, file.getInputStream());
                }

                //存储至mysql
                Document document = new Document();
                document.setDatasetId(dataset.getId());
                document.setName(file.getOriginalFilename());
                document.setType(type);
                document.setFileKey(fileKey);
                document.setSize(file.getSize());
                document.setTags(JsonUtil.toJsonString(tags));
                document.setType(type);
                document.setHashCode(fileHash);
                document.setState(DocumentStateEnum.PROCESSING.value());
                this.save(document);
                //向量化至qdrant
                documentService.toQdrant(dataset.getName(), file.getInputStream(), vectorType, type, document, tags);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("File read exception:", e);
        }
    }

    /**
     * 向量化至qdrant
     *
     * @param datasetName
     * @param fileInputStream
     * @param vectorType
     * @param type
     * @param document
     * @throws IOException
     */
    @Async
    public void toQdrant(String datasetName, InputStream fileInputStream, String vectorType, String type, Document document, List<String> tags) {
        List<String> qdrantIds = documentQdrantService.getQdrantIds(document.getId());
        List<DocumentQdrant> documentQdrants = null;
        if (qdrantIds.size() == 0) {
            FileInterface fileService = fileFactory.getFileService(type);
            try {
                documentQdrants = fileService.split(fileInputStream, document);
            } catch (Exception e) {
                return;
            }
        } else {
            documentQdrants = documentQdrantService.getByDocumentId(document.getId());
        }
        toVector(documentQdrants, document, vectorType, datasetName, tags);
    }


    private List<Document> getByHash(String fileHash) {
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Document::getHashCode, fileHash);
        return this.list(queryWrapper);
    }

    /**
     * documentQdrants转化为vector
     *
     * @param documentQdrants
     * @param vectorType
     */
    @Async
    public void toVector(List<DocumentQdrant> documentQdrants, Document document, String vectorType, String datasetName, List<String> tags) {
        Dataset dataset = datasetService.getByName(datasetName);
        try {
            QdrantService qdrantService = new QdrantService();
            VectorInterface vectorService = vectorFactory.getVectorService(vectorType);
            for (DocumentQdrant documentQdrant : documentQdrants) {
                if (Objects.equals(documentQdrant.getState(), DocumentStateEnum.COMPLETE.value())) {
                    continue;
                }
                List<Point> points = new ArrayList<>();
                String info = documentQdrant.getInfo();
                List<VectorResult> vectorResultList = vectorService.getVector(info, dataset.getVectorType());
                StringBuilder ids = new StringBuilder(documentQdrant.getQdrantId());
                for (int i = 0; i < vectorResultList.size(); i++) {
                    VectorResult vectorResult = vectorResultList.get(i);
                    //存储至qdrant
                    Payload payload = new Payload();
                    payload.setTags(tags);
                    payload.setContext(vectorResult.getText());
                    payload.setType(PayloadTypeEnum.FILE.value());
                    payload.setDocumentId(document.getId());
                    Point point = new Point();
                    String id = documentQdrant.getQdrantId();
                    if (i != 0) {
                        id = UUID.randomUUID().toString();
                        ids.append(",");
                        ids.append(id);
                    }
                    point.setId(id);
                    point.setVector(vectorResult.getVector());
                    point.setPayload(payload);
                    points.add(point);
                }
                documentQdrant.setQdrantId(ids.toString());
                documentQdrant.setState(DocumentStateEnum.COMPLETE.value());
                documentQdrantService.updateById(documentQdrant);
                PointCreateRequest request = new PointCreateRequest();
                request.setPoints(points);
                qdrantService.createPoints(datasetName, request);
            }
            document.setState(DocumentStateEnum.COMPLETE.value());
            this.updateById(document);
        } catch (Exception e) {
            log.error("txt file converted to qdrant exception", e);
            document.setState(DocumentStateEnum.ERROR.value());
            this.updateById(document);
        }

    }

    /**
     * 分页查询
     *
     * @param datasetName
     * @param pageParam
     * @param page
     * @return
     */
    public IPage<Document> pageByQuery(String datasetName, DocumentQuery pageParam, IPage<Document> page) {
        return baseMapper.pageByQuery(datasetName, pageParam, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, String name) {
        Document document = this.getById(id);
        if (document == null) {
            throw new BusinessException("file does not exist");
        }
        this.removeById(id);
        //删除qdrant
        List<String> qdrantStrIds = documentQdrantService.getQdrantIds(document.getId());
        List<String> qdrantIds = new ArrayList<>();
        for (String qdrantIdStr : qdrantStrIds) {
            String[] split = qdrantIdStr.split(",");
            if (split.length > 0) {
                qdrantIds.addAll(Arrays.asList(split));
            }
        }

        if (qdrantIds.size() > 0) {
            QdrantService qdrantService = new QdrantService();
            qdrantService.deletePoints(name, qdrantIds);
        }
        documentQdrantService.deleteByDocumentId(document.getId());

        //删除oss
        s3Service.removeObject(StorageUtils.BUCKET_NAME, document.getFileKey());
    }

    /**
     * 重新处理
     *
     * @param name
     * @param id
     */
    public void reprocess(String name, Long id) {
        Dataset dataset = datasetService.getByName(name);
        if (dataset == null) {
            throw new BusinessException("dataset does not exist");
        }
        Document document = this.getById(id);
        if (document == null) {
            throw new BusinessException("file does not exist");
        }
        if (Objects.equals(document.getState(), DocumentStateEnum.COMPLETE.value())) {
            throw new BusinessException("file has been COMPLETE");
        }
        if (Objects.equals(document.getState(), DocumentStateEnum.PROCESSING.value())) {
            throw new BusinessException("file is being PROCESSING");
        }
        //删除qdrant
        List<String> qdrantIds = documentQdrantService.getQdrantIds(document.getId());
        if (qdrantIds.size() > 0) {
            QdrantService qdrantService = new QdrantService();
            qdrantService.deletePoints(name, qdrantIds);
        }
        document.setState(DocumentStateEnum.PROCESSING.value());
        this.updateById(document);
        String fileUrl = s3Service.getObjectURL(StorageUtils.BUCKET_NAME, document.getFileKey(), StorageUtils.DOWNLOAD_EXPIRY_TIME);
        try {
            URL url = new URL(fileUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            String tags = document.getTags();
            documentService.toQdrant(dataset.getName(), inputStream, dataset.getVectorType(), document.getType(), document, JsonUtil.toArray(tags, String.class));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("File read exception:", e);
        }
    }

    public List<DocumentVO> listByQuery(String name, DocumentQuery pageParam) {
        List<Document> documents = baseMapper.listByQuery(name, pageParam);
        List<DocumentVO> documentVOS = new ArrayList<>();
        for (Document document : documents) {
            DocumentVO documentVO = new DocumentVO();
            documentVO.conver(document);
            documentVOS.add(documentVO);
        }
        return documentVOS;
    }

    public void deleteByDatasetId(Long id) {
        List<Document> documents = this.listByDatasetId(id);
        if (documents.size() > 0) {
            for (Document document : documents) {
                this.removeById(document.getId());
                documentQdrantService.deleteByDocumentId(document.getId());
            }
        }
    }

    public List<Document> listByDatasetId(Long id) {
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Document::getDatasetId, id);
        return this.list(queryWrapper);
    }
}
