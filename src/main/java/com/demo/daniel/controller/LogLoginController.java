package com.demo.daniel.controller;

import com.demo.daniel.convert.LogLoginConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.LogLoginQueryDTO;
import com.demo.daniel.model.vo.LogLoginVO;
import com.demo.daniel.service.LogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/log/login")
public class LogLoginController {

    @Autowired
    private LogLoginService logLoginService;

    @GetMapping
    // @PreAuthorize("hasAuthority('log:login:search')")
    public ApiResponse<Page<LogLoginVO>> getLogs(@ModelAttribute LogLoginQueryDTO request) {
        Page<LogLoginVO> logs = logLoginService.getLogs(request).map(LogLoginConvert::convertToVO);
        return ApiResponse.ok(logs);
    }
}
