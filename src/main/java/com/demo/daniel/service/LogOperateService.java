package com.demo.daniel.service;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.LogOperateQueryDTO;
import com.demo.daniel.model.entity.LogOperate;
import com.demo.daniel.repository.LogOperateRepository;
import com.demo.daniel.util.LogOperateSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class LogOperateService {

    @Autowired
    private LogOperateRepository logOperateRepository;

    public Page<LogOperate> getLogs(LogOperateQueryDTO request) {
        Specification<LogOperate> spec = LogOperateSpecifications.buildSpecification(request.getUsername(),
                request.getModule(), request.getRequestUri(), request.getStatus());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return logOperateRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), sort));
    }

    public LogOperate getLog(Long id) {
        return logOperateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOG_OPERATE_NOT_EXIST.getCode(), "Log Operate " + id + " not found"));
    }
}
