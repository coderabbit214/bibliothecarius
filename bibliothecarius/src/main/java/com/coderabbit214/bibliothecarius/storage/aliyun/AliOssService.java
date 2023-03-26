package com.coderabbit214.bibliothecarius.storage.aliyun;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.storage.FormData;
import com.coderabbit214.bibliothecarius.storage.StorageConstantPropertiesUtils;
import com.coderabbit214.bibliothecarius.storage.StorageInterface;
import com.coderabbit214.bibliothecarius.storage.StorageRequestData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.UUID;

/**
 * @author Mr_J
 */
@Service
public class AliOssService implements StorageInterface {

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String objectName) {
        LocalDateTime now = LocalDateTime.now();
        objectName = now.format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd/HH/mm/ss").toFormatter()) + "/"+ objectName;
        OSS ossClient = new OSSClientBuilder().build(AliyunConstantPropertiesUtils.END_POINT, AliyunConstantPropertiesUtils.KEY_ID, AliyunConstantPropertiesUtils.KEY_SECRET);
        try {
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            //第一个参数 Bucket名称
            //第二个参数 文件路径和文件名称
            //第三个参数 文件输入流
            ossClient.putObject(AliyunConstantPropertiesUtils.BUCKET_NAME, objectName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //上传之后的文件路径返回
            //需要手动拼接
            return objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断文件是否存在
     */
    @Override
    public boolean existFile(String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(AliyunConstantPropertiesUtils.END_POINT, AliyunConstantPropertiesUtils.KEY_ID, AliyunConstantPropertiesUtils.KEY_SECRET);
        boolean found = ossClient.doesObjectExist(AliyunConstantPropertiesUtils.BUCKET_NAME, objectName);
        ossClient.shutdown();
        return found;
    }

    /**
     * 签名
     */
    @Override
    public StorageRequestData getPolicy(String dir, String fileName){
        fileName = UUID.randomUUID()+"_"+fileName;
        String host = StorageConstantPropertiesUtils.PROTOCOL + AliyunConstantPropertiesUtils.BUCKET_NAME + "." + AliyunConstantPropertiesUtils.END_POINT;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(AliyunConstantPropertiesUtils.END_POINT, AliyunConstantPropertiesUtils.KEY_ID, AliyunConstantPropertiesUtils.KEY_SECRET);
        StorageRequestData ossRequestData =null;
        try {
            long expireEndTime = System.currentTimeMillis() + StorageConstantPropertiesUtils.UPDATE_EXPIRY_TIME * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir+fileName);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            FormData formData = new FormData(AliyunConstantPropertiesUtils.KEY_ID,encodedPolicy,postSignature,dir,dir+fileName,fileName,String.valueOf(expireEndTime / 1000));
            ossRequestData = new StorageRequestData(host,"file",true,"post",formData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return ossRequestData;
    }

    @Override
    public void removeObject(String objectName) {
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(AliyunConstantPropertiesUtils.END_POINT, AliyunConstantPropertiesUtils.KEY_ID, AliyunConstantPropertiesUtils.KEY_SECRET);
            ossClient.deleteObject(AliyunConstantPropertiesUtils.BUCKET_NAME, objectName);
        } catch (OSSException oe) {
            throw new BusinessException("OSS handles exceptions：" + "Error Message:" + oe.getErrorMessage() + "Error Code:" + oe.getErrorCode() + "Request ID:" + oe.getRequestId() + "Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            ce.printStackTrace();
            throw new BusinessException("Connection failed");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 获取签名授权访问
     * @param objectName
     * @return
     */
    @Override
    public String getExpiration(String objectName){
        OSS ossClient = new OSSClientBuilder().build(AliyunConstantPropertiesUtils.END_POINT, AliyunConstantPropertiesUtils.KEY_ID, AliyunConstantPropertiesUtils.KEY_SECRET);
        Date expiration = new Date(System.currentTimeMillis() + StorageConstantPropertiesUtils.DOWNLOAD_EXPIRY_TIME * 1000);
        URL url = ossClient.generatePresignedUrl(AliyunConstantPropertiesUtils.BUCKET_NAME, objectName, expiration);
        ossClient.shutdown();
        return url.toString();
    }

}
