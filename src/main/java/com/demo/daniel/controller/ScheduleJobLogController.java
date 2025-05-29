package com.demo.daniel.controller;

import com.demo.daniel.convert.ScheduleJobLogConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.ScheduleJobLogQueryDTO;
import com.demo.daniel.model.entity.ScheduleJobLog;
import com.demo.daniel.model.vo.ScheduleJobLogVO;
import com.demo.daniel.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule/log")
public class ScheduleJobLogController {

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    @GetMapping
    @PreAuthorize("hasAuthority('job:log:search')")
    public ApiResponse<Page<ScheduleJobLogVO>> getLogs(@ModelAttribute ScheduleJobLogQueryDTO request) {
        Page<ScheduleJobLogVO> logs = scheduleJobLogService.getLogs(request).map(ScheduleJobLogConvert::convertToVO);
        return ApiResponse.ok(logs);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('job:log:view')")
    public ApiResponse<ScheduleJobLogVO> getLog(@PathVariable("id") Long id) {
        ScheduleJobLog log = scheduleJobLogService.getLog(id);
        return ApiResponse.ok(ScheduleJobLogConvert.convertToVO(log));
    }
}
