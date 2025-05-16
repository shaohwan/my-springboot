package com.demo.daniel.service;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.AttachmentQueryDTO;
import com.demo.daniel.model.entity.Attachment;
import com.demo.daniel.repository.AttachmentRepository;
import com.demo.daniel.util.AttachmentSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public abstract class FileService {

    @Autowired
    protected AttachmentRepository attachmentRepository;

    @Value("${cloud.provider}")
    protected String platform;

    public abstract void uploadFile(MultipartFile file);

    public abstract InputStream downloadFile(String name);

    public Page<Attachment> getFiles(AttachmentQueryDTO request) {
        Specification<Attachment> spec = AttachmentSpecifications.buildSpecification(request.getName(), request.getPlatform());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return attachmentRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), sort));
    }

    public Attachment getFile(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_EXIST.getCode(), "File ID " + id + " not found"));
    }

    public abstract void deleteFiles(List<Long> ids);
}
