package com.coderabbit214.bibliothecarius.storage.minio;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.storage.FormData;
import com.coderabbit214.bibliothecarius.storage.StorageConstantPropertiesUtils;
import com.coderabbit214.bibliothecarius.storage.StorageInterface;
import com.coderabbit214.bibliothecarius.storage.StorageRequestData;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.UUID;

/**
 * @author Mr_J
 */
@Service
public class MinIOOssService implements StorageInterface {

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String objectName) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MinIOConstantPropertiesUtils.END_POINT)
                .credentials(MinIOConstantPropertiesUtils.KEY_ID, MinIOConstantPropertiesUtils.KEY_SECRET)
                .build();
        LocalDateTime now = LocalDateTime.now();
        objectName = now.format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd/HH/mm/ss").toFormatter()) + "/"+ objectName;

        InputStream in = null;
        try {
            in = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder().bucket(MinIOConstantPropertiesUtils.BUCKET_NAME).object(objectName)
                    .stream(in, file.getSize(), -1).contentType(file.getContentType()).build());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("file exception");
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
            throw new RuntimeException("upload exception");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objectName;
    }

    /**
     * 判断文件是否存在
     */
    @Override
    public boolean existFile(String objectName) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MinIOConstantPropertiesUtils.END_POINT)
                .credentials(MinIOConstantPropertiesUtils.KEY_ID, MinIOConstantPropertiesUtils.KEY_SECRET)
                .build();

        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(MinIOConstantPropertiesUtils.BUCKET_NAME)
                    .object(objectName).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 上传签名
     */
    @Override
    public StorageRequestData getPolicy(String dir, String fileName) {
        fileName = UUID.randomUUID() + "_" + fileName;
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MinIOConstantPropertiesUtils.END_POINT)
                .credentials(MinIOConstantPropertiesUtils.KEY_ID, MinIOConstantPropertiesUtils.KEY_SECRET)
                .build();
        String product = null;
        StorageRequestData ossRequestData = null;
        try {
            product = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(MinIOConstantPropertiesUtils.BUCKET_NAME)
                            .object(dir + fileName)
                            .expiry(StorageConstantPropertiesUtils.UPDATE_EXPIRY_TIME)
                            .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            e.printStackTrace();
            throw new RuntimeException("get signature exception");
        }
        FormData formData = new FormData(MinIOConstantPropertiesUtils.KEY_ID, null, null, dir, dir + fileName, fileName, null);
        ossRequestData = new StorageRequestData(product, "data", false, "put", formData);
        return ossRequestData;
    }

    @Override
    public void removeObject(String objectName) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MinIOConstantPropertiesUtils.END_POINT)
                .credentials(MinIOConstantPropertiesUtils.KEY_ID, MinIOConstantPropertiesUtils.KEY_SECRET)
                .build();
        try {
            minioClient
                    .removeObject(RemoveObjectArgs.builder().bucket(MinIOConstantPropertiesUtils.BUCKET_NAME).object(objectName).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            e.printStackTrace();
            throw new BusinessException("failed to delete");
        }
    }

    /**
     * 查看签名
     */
    @Override
    public String getExpiration(String objectName) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MinIOConstantPropertiesUtils.END_POINT)
                .credentials(MinIOConstantPropertiesUtils.KEY_ID, MinIOConstantPropertiesUtils.KEY_SECRET)
                .build();
        String presignedObjectUrl = null;
        try {
            presignedObjectUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(MinIOConstantPropertiesUtils.BUCKET_NAME)
                            .object(objectName)
                            .expiry(StorageConstantPropertiesUtils.DOWNLOAD_EXPIRY_TIME)
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            e.printStackTrace();
            throw new RuntimeException("get signature exception");
        }
        return presignedObjectUrl;
    }
}
