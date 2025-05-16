package com.demo.daniel.service;

import com.aliyun.oss.OSS;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.entity.Attachment;
import com.demo.daniel.properties.AliyunProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.demo.daniel.util.AppConstants.FILE_PREFIX;

@Service
@ConditionalOnProperty(name = "cloud.provider", havingValue = "aliyun")
public class OssService extends FileService {

    @Autowired
    private OSS ossClient;
    @Autowired
    private AliyunProperties aliyunProperties;

    @Override
    public void uploadFile(MultipartFile file) {
        String bucketName = aliyunProperties.getOss().getBucket();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String key = FILE_PREFIX + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(bucketName, key, inputStream);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getLocalizedMessage());
        }

        String url = String.format("https://%s.%s/%s", bucketName, aliyunProperties.getOss().getEndpoint(), key);
        saveFile(fileName, url, file.getSize(), platform);
    }

    @Override
    public InputStream downloadFile(String name) {
        String bucketName = aliyunProperties.getOss().getBucket();
        String key = FILE_PREFIX + name;

        return ossClient.getObject(bucketName, key).getObjectContent();
    }

    @Override
    public void deleteFiles(List<Long> ids) {
        String bucketName = aliyunProperties.getOss().getBucket();
        ids.forEach(id -> {
            Attachment attachment = getFile(id);
            ossClient.deleteObject(bucketName, FILE_PREFIX + attachment.getName());
            attachmentRepository.deleteById(id);
        });
    }
}
