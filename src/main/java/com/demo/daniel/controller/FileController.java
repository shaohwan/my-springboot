package com.demo.daniel.controller;

import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.convert.AttachmentConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.AttachmentQueryDTO;
import com.demo.daniel.model.entity.Attachment;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.vo.AttachmentVO;
import com.demo.daniel.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('file:upload')")
    @OperateLog(module = "文件管理", name = "上传文件", type = LogOperateType.ADD)
    public ApiResponse<Void> uploadFile(@RequestParam("file") MultipartFile file) {
        fileService.uploadFile(file);
        return ApiResponse.ok();
    }

    @GetMapping
    // @PreAuthorize("hasAuthority('file:search')")
    public ApiResponse<Page<AttachmentVO>> getFiles(@ModelAttribute AttachmentQueryDTO request) {
        Page<AttachmentVO> attachments = fileService.getFiles(request).map(AttachmentConvert::convertToVO);
        return ApiResponse.ok(attachments);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAuthority('file:download')")
    @OperateLog(module = "文件管理", name = "下载文件", type = LogOperateType.OTHER)
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) {
        Attachment attachment = fileService.getFile(id);
        String name = attachment.getName();
        InputStreamResource resource = new InputStreamResource(fileService.downloadFile(name));
        // Encode filename for Content-Disposition (RFC 5987)
        String encodedFilename = URLEncoder.encode(name, StandardCharsets.UTF_8)
                .replace("+", "%20"); // Replace '+' with '%20' for spaces

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('file:delete')")
    @OperateLog(module = "文件管理", name = "删除文件(们)", type = LogOperateType.DELETE)
    public ApiResponse<Void> deleteFiles(@RequestBody List<Long> ids) {
        fileService.deleteFiles(ids);
        return ApiResponse.ok();
    }
}
