package com.demo.daniel.service;

import com.demo.daniel.model.dto.LogLoginQueryDTO;
import com.demo.daniel.model.entity.LogLogin;
import com.demo.daniel.model.entity.LogLoginOperationType;
import com.demo.daniel.model.entity.LogLoginStatusType;
import com.demo.daniel.repository.LogLoginRepository;
import com.demo.daniel.util.ClientRequestUtils;
import com.demo.daniel.util.LogLoginSpecifications;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class LogLoginService {

    @Autowired
    private LogLoginRepository logLoginRepository;

    public void saveLog(String username, LogLoginStatusType status, LogLoginOperationType operation) {
        HttpServletRequest request = ClientRequestUtils.getCurrentRequest();
        String ip = ClientRequestUtils.getClientIp(request);

        LogLogin log = new LogLogin();
        log.setUsername(username);
        log.setIp(ip);
        log.setAddress(ClientRequestUtils.getLocationByIp(ip));
        log.setUserAgent(ClientRequestUtils.getUserAgent(request));
        log.setStatus(status);
        log.setOperation(operation);

        logLoginRepository.save(log);
    }

    public Page<LogLogin> getLogs(LogLoginQueryDTO request) {
        Specification<LogLogin> spec = LogLoginSpecifications.buildSpecification(request.getUsername());
        return logLoginRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize()));
    }
}
