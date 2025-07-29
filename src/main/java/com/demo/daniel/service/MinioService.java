package com.demo.daniel.service;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.entity.Attachment;
import com.demo.daniel.properties.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@ConditionalOnProperty(name = "cloud.provider", havingValue = "minio")
@Slf4j
public class MinioService extends FileService {

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;

    @Override
    public void uploadFile(MultipartFile file) {
        String bucketName = minioProperties.getBucket();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Failed to upload file to MinIO: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

        String url = minioProperties.getEndpoint() + "/" + bucketName + "/" + fileName;
        saveFile(fileName, url, file.getSize(), platform);
    }

    @Override
    public InputStream downloadFile(String name) {
        String bucketName = minioProperties.getBucket();

        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(name)
                            .build()
            );
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Failed to download file from MinIO: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public void deleteFiles(List<Long> ids) {
        String bucketName = minioProperties.getBucket();
        ids.forEach(id -> {
            Attachment attachment = getFile(id);
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(attachment.getName())
                                .build()
                );
                attachmentRepository.deleteById(id);
            } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
                log.error("Failed to delete file from MinIO: {}", e.getMessage());
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
            }
        });
    }
}
