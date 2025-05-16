package com.demo.daniel.service;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.entity.Attachment;
import com.demo.daniel.properties.AwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.demo.daniel.util.AppConstants.S3_BUCKET_PREFIX;

@Service
@ConditionalOnProperty(name = "cloud.provider", havingValue = "aws")
@Slf4j
public class S3Service extends FileService {

    @Autowired
    private S3Client s3Client;
    @Autowired
    private AwsProperties awsProperties;

    @Override
    public void uploadFile(MultipartFile file) {
        String bucketName = awsProperties.getS3().getBucket();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String key = S3_BUCKET_PREFIX + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getLocalizedMessage());
        }

        Attachment attachment = new Attachment();
        attachment.setName(fileName);
        attachment.setUrl(s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm());
        attachment.setSize(file.getSize());
        attachment.setPlatform(platform);
        attachmentRepository.save(attachment);
    }

    @Override
    public InputStream downloadFile(String name) {
        String bucketName = awsProperties.getS3().getBucket();
        String key = S3_BUCKET_PREFIX + name;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public void deleteFiles(List<Long> ids) {
        String bucketName = awsProperties.getS3().getBucket();
        ids.forEach(id -> {
            Attachment attachment = getFile(id);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(S3_BUCKET_PREFIX + attachment.getName())
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            attachmentRepository.deleteById(id);
        });
    }
}
