package com.demo.daniel.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@ConditionalOnProperty(name = "cloud.provider", havingValue = "local")
public class LocalService extends FileService {

    @Override
    public void uploadFile(MultipartFile file) {
    }

    @Override
    public InputStream downloadFile(String name) {
        return null;
    }

    @Override
    public void deleteFiles(List<Long> ids) {

    }
}
