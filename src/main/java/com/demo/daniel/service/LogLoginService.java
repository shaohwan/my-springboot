package com.demo.daniel.service;

import com.demo.daniel.convert.LogLoginConvert;
import com.demo.daniel.model.dto.LogLoginQueryDTO;
import com.demo.daniel.model.entity.LogLogin;
import com.demo.daniel.model.entity.LogLoginOperation;
import com.demo.daniel.model.entity.LogStatus;
import com.demo.daniel.model.vo.LogLoginVO;
import com.demo.daniel.repository.LogLoginRepository;
import com.demo.daniel.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogLoginService {

    @Autowired
    private LogLoginRepository logLoginRepository;

    public void saveLog(String username, LogStatus status, LogLoginOperation operation) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ip = IpUtils.getClientIp(request);

        LogLogin log = new LogLogin();
        log.setUsername(username);
        log.setIp(ip);
        log.setAddress(IpUtils.getLocationByIp(ip));
        log.setUserAgent(IpUtils.getUserAgent(request));
        log.setStatus(status);
        log.setOperation(operation);

        logLoginRepository.save(log);
    }

    public Page<LogLogin> getLogs(LogLoginQueryDTO request) {
        Specification<LogLogin> spec = LogLoginSpecifications.buildSpecification(request.getUsername(), request.getStartTime(), request.getEndTime());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return logLoginRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), sort));
    }

    public void exportLogs() {
        List<LogLoginVO> logs = logLoginRepository.findAll().stream().map(LogLoginConvert::convertToVO).collect(Collectors.toList());
        ExcelUtils.exportExcel(logs, AppConstants.LOG_LOGIN_EXCEL, null, LogLoginVO.class);
    }
}
