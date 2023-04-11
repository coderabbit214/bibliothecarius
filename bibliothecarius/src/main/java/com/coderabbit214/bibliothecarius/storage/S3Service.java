package com.coderabbit214.bibliothecarius.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class S3Service implements OssService {

    private final AmazonS3 amazonS3;

    public S3Service(
            @Value("${storage.keyid}") String keyId,
            @Value("${storage.keysecret}") String keySecret,
            @Value("${storage.endpoint}") String endpoint
    ) {
        AWSCredentials credentials = new BasicAWSCredentials(keyId, keySecret);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, ""))
                .build();
    }

    @Override
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    @Override
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    @Override
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream, String contextType) {
        PutObjectResult putObjectResult = putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    @Override
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream) {
        putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    @Override
    @SneakyThrows
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    @Override
    @SneakyThrows
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    @Override
    @SneakyThrows
    public void removeObject(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

    @Override
    @SneakyThrows
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }

    @SneakyThrows
    private PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
                                      String contextType) {
        byte[] bytes = IOUtils.toByteArray(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
    }
}
