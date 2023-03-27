package com.coderabbit214.bibliothecarius.dataset.document;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.dataset.Dataset;
import com.coderabbit214.bibliothecarius.dataset.DatasetService;
import com.coderabbit214.bibliothecarius.qdrant.QdrantService;
import com.coderabbit214.bibliothecarius.qdrant.point.Point;
import com.coderabbit214.bibliothecarius.qdrant.point.PointCreateRequest;
import com.coderabbit214.bibliothecarius.storage.StorageFactory;
import com.coderabbit214.bibliothecarius.storage.StorageInterface;
import com.coderabbit214.bibliothecarius.vector.VectorFactory;
import com.coderabbit214.bibliothecarius.vector.VectorInterface;
import com.coderabbit214.bibliothecarius.vector.VectorResult;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    private static final String FILE_TYPE_TXT = "txt";

    private static final String FILE_TYPE_MD = "md";

    private static final List<String> SUPPORT_FILE_TYPE = List.of(FILE_TYPE_TXT, FILE_TYPE_MD);

    private final StorageFactory storageFactory;

    private final VectorFactory vectorFactory;

    private final DatasetService datasetService;

    private final DocumentService documentService;

    private final DocumentQdrantService documentQdrantService;

    public DocumentService(StorageFactory storageFactory, VectorFactory vectorFactory, DatasetService datasetService, @Lazy DocumentService documentService, DocumentQdrantService documentQdrantService) {
        this.storageFactory = storageFactory;
        this.vectorFactory = vectorFactory;
        this.datasetService = datasetService;
        this.documentService = documentService;
        this.documentQdrantService = documentQdrantService;
    }

    /**
     * 文件数据上传
     *
     * @param name
     * @param file
     */
    public void updateFile(String name, MultipartFile file) {
        Dataset dataset = datasetService.getByName(name);
        if (dataset == null) {
            throw new BusinessException("dataset does not exist");
        }
        documentService.add(dataset, file, dataset.getVectorType());
    }

    /**
     * 文件上传
     *
     * @param dataset
     * @param file
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Dataset dataset, MultipartFile file, String vectorType) {
        //判读文件类型
        String type;
        String fileHash;
        try {
            //根据后缀读取文件类型
            String[] nameSplit = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            type = nameSplit[nameSplit.length - 1];
            fileHash = DigestUtils.md5Hex(file.getInputStream());

            if (!SUPPORT_FILE_TYPE.contains(type)) {
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
                StorageInterface ossService = storageFactory.getOssService();
                fileKey = ossService.uploadFile(file, file.getOriginalFilename());
            }

            //存储至mysql
            Document document = new Document();
            document.setDatasetId(dataset.getId());
            document.setName(file.getOriginalFilename());
            document.setType(type);
            document.setFileKey(fileKey);
            document.setSize(file.getSize());
            document.setType(type);
            document.setHashCode(fileHash);
            document.setState(DocumentStateEnum.PROCESSING.value());
            this.save(document);
            //向量化至qdrant
            toQdrant(dataset.getName(), file.getInputStream(), vectorType, type, document, true);
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
    private void toQdrant(String datasetName, InputStream fileInputStream, String vectorType, String type, Document document, boolean init) throws IOException {
        switch (type) {
            case FILE_TYPE_TXT:
                List<DocumentQdrant> documentQdrants = null;
                if (init) {
                    documentQdrants = this.txtSplit(fileInputStream, document);
                } else {
                    documentQdrants = documentQdrantService.getByDocumentId(document.getId());
                }
                documentService.txtToQdrant(documentQdrants, document, vectorType, datasetName);
                break;
            case FILE_TYPE_MD:
                documentService.mdToQdrant(fileInputStream, document, vectorType, datasetName);
                break;
            default:
                throw new BusinessException("unsupported file type");
        }
    }

    /**
     * md文件转化为qdrant
     *
     * @param fileInputStream
     * @param document
     * @param vectorType
     * @param datasetName
     */
    @Async
    public void mdToQdrant(InputStream fileInputStream, Document document, String vectorType, String datasetName) {
        BufferedReader reader = null;
        try {
            VectorInterface vectorService = vectorFactory.getVectorService(vectorType);
            QdrantService qdrantService = new QdrantService();
            //读取文件内容
            reader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            List<Point> points = new ArrayList<>();
            List<DocumentQdrant> documentQdrants = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("## ")) {
                    List<VectorResult> vectorResultList = vectorService.getVector(stringBuilder.toString());
                    for (VectorResult vectorResult : vectorResultList) {
                        //存储至qdrant
                        QdrantDocument qdrantDocument = new QdrantDocument();
                        qdrantDocument.setDocumentId(document.getId());
                        qdrantDocument.setContext(vectorResult.getText());
                        Point point = new Point();
                        String id = UUID.randomUUID().toString();
                        point.setId(id);
                        point.setVector(vectorResult.getVector());
                        point.setPayload(qdrantDocument);
                        points.add(point);
                        DocumentQdrant documentQdrant = new DocumentQdrant();
                        documentQdrant.setDocumentId(document.getId());
                        documentQdrant.setQdrantId(id);
                        documentQdrants.add(documentQdrant);
                    }
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append(line).append('\n');
            }

            PointCreateRequest request = new PointCreateRequest();
            request.setPoints(points);
            qdrantService.createPoints(datasetName, request);
            document.setState(DocumentStateEnum.COMPLETE.value());
            this.updateById(document);
            documentQdrantService.saveBatch(documentQdrants);
        } catch (Exception e) {
            log.error("md file converted to qdrant exception", e);
            document.setState(DocumentStateEnum.ERROR.value());
            this.updateById(document);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private List<Document> getByHash(String fileHash) {
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Document::getHashCode, fileHash);
        return this.list(queryWrapper);
    }

    /**
     * txt 文件分隔
     *
     * @param fileInputStream
     * @param document
     * @return
     */
    public List<DocumentQdrant> txtSplit(InputStream fileInputStream, Document document) {
        try {
            //读取文件内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = reader.readLine();
            List<DocumentQdrant> documentQdrants = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            while (line != null) {
                //根据句子分隔
                if ("".equals(line)) {
                    line = reader.readLine();
                    continue;
                }
                stringBuilder.append(line);
                //去除末尾所有空格
                line = line.trim();
                if (!line.endsWith("。") && !line.endsWith("！") && !line.endsWith("？") && !line.endsWith(".") && !line.endsWith("!") && !line.endsWith("?")) {
                    line = reader.readLine();
                    continue;
                }
                DocumentQdrant documentQdrant = new DocumentQdrant();
                documentQdrant.setDocumentId(document.getId());
                String id = UUID.randomUUID().toString();
                documentQdrant.setQdrantId(id);
                documentQdrant.setInfo(stringBuilder.toString());
                documentQdrant.setState(DocumentStateEnum.PROCESSING.value());
                documentQdrants.add(documentQdrant);
                line = reader.readLine();
                stringBuilder = new StringBuilder();
            }
            documentQdrantService.saveBatch(documentQdrants);
            return documentQdrants;
        } catch (Exception e) {
            log.error("txt file converted to qdrant exception", e);
            document.setState(DocumentStateEnum.ERROR.value());
            this.updateById(document);
            throw new BusinessException("txt file converted to qdrant exception");
        }
    }

    /**
     * txt文件转换为qdrant
     *
     * @param documentQdrants
     * @param vectorType
     */
    @Async
    public void txtToQdrant(List<DocumentQdrant> documentQdrants, Document document, String vectorType, String datasetName) {

        try {
            QdrantService qdrantService = new QdrantService();
            VectorInterface vectorService = vectorFactory.getVectorService(vectorType);
            for (DocumentQdrant documentQdrant : documentQdrants) {
                if (Objects.equals(documentQdrant.getState(), DocumentStateEnum.COMPLETE.value())) {
                    continue;
                }
                List<Point> points = new ArrayList<>();
                String info = documentQdrant.getInfo();
                List<VectorResult> vectorResultList = vectorService.getVector(info);
                StringBuilder ids = new StringBuilder(documentQdrant.getQdrantId());
                for (int i = 0; i < vectorResultList.size(); i++) {
                    VectorResult vectorResult = vectorResultList.get(i);
                    //存储至qdrant
                    QdrantDocument qdrantDocument = new QdrantDocument();
                    qdrantDocument.setDocumentId(documentQdrant.getDocumentId());
                    qdrantDocument.setContext(vectorResult.getText());
                    Point point = new Point();
                    String id = documentQdrant.getQdrantId();
                    if (i != 0) {
                        id = UUID.randomUUID().toString();
                        ids.append(",");
                        ids.append(id);
                    }
                    point.setId(id);
                    point.setVector(vectorResult.getVector());
                    point.setPayload(qdrantDocument);
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
            documentQdrantService.saveBatch(documentQdrants);
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
        List<String> qdrantIds = documentQdrantService.getQdrantIds(document.getId());
        if (qdrantIds.size() > 0) {
            QdrantService qdrantService = new QdrantService();
            qdrantService.deletePoints(name, qdrantIds);
        }

        //删除oss
        StorageInterface ossService = storageFactory.getOssService();
        ossService.removeObject(document.getFileKey());
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
        String fileKey = document.getFileKey();
        String fileUrl = storageFactory.getOssService().getExpiration(fileKey);
        try {
            URL url = new URL(fileUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            toQdrant(dataset.getName(), inputStream, dataset.getVectorType(), document.getType(), document, false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("File read exception:", e);
        }
    }

    public List<Document> listByQuery(String name, DocumentQuery pageParam) {
        return baseMapper.listByQuery(name, pageParam);
    }
}
