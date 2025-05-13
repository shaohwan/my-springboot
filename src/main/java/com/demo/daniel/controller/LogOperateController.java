package com.demo.daniel.controller;

import com.demo.daniel.convert.LogOperateConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.LogOperateQueryDTO;
import com.demo.daniel.model.entity.LogOperate;
import com.demo.daniel.model.vo.LogOperateVO;
import com.demo.daniel.service.LogOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log/operate")
public class LogOperateController {

    @Autowired
    private LogOperateService logOperateService;

    @GetMapping
    // @PreAuthorize("hasAuthority('log:operate:search')")
    public ApiResponse<Page<LogOperateVO>> getLogs(@ModelAttribute LogOperateQueryDTO request) {
        Page<LogOperateVO> logs = logOperateService.getLogs(request).map(LogOperateConvert::convertToVO);
        return ApiResponse.ok(logs);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('log:operate:view')")
    public ApiResponse<LogOperateVO> getLog(@PathVariable Long id) {
        LogOperate log = logOperateService.getLog(id);
        return ApiResponse.ok(LogOperateConvert.convertToVO(log));
    }
}
