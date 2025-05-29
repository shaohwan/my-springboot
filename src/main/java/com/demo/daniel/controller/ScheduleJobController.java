package com.demo.daniel.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.convert.ScheduleJobConvert;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.ScheduleJobQueryDTO;
import com.demo.daniel.model.dto.ScheduleJobUpsertDTO;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.vo.ScheduleJobVO;
import com.demo.daniel.service.ScheduleJobService;
import com.demo.daniel.util.CronUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    @GetMapping
    // @PreAuthorize("hasAuthority('user:search')")
    public ApiResponse<Page<ScheduleJobVO>> getJobs(@ModelAttribute ScheduleJobQueryDTO request) {
        Page<ScheduleJobVO> jobs = scheduleJobService.getJobs(request).map(ScheduleJobConvert::convertToVO);
        return ApiResponse.ok(jobs);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('job:add')")
    @OperateLog(module = "定时任务", name = "新增任务", type = LogOperateType.ADD)
    public ApiResponse<Void> createJob(@RequestBody ScheduleJobUpsertDTO request) {
        if (!CronUtils.isValid(request.getCronExpression())) {
            return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "操作失败, Cron表达式不正确");
        }
        checkBean(request.getBeanName(), request.getMethod());
        scheduleJobService.upsertJob(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('job:edit')")
    @OperateLog(module = "定时任务", name = "修改任务", type = LogOperateType.EDIT)
    public ApiResponse<Void> updateJob(@RequestBody ScheduleJobUpsertDTO request) {
        if (!CronUtils.isValid(request.getCronExpression())) {
            return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "操作失败, Cron表达式不正确");
        }
        checkBean(request.getBeanName(), request.getMethod());
        scheduleJobService.upsertJob(request);
        return ApiResponse.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('job:delete')")
    @OperateLog(module = "定时任务", name = "删除任务", type = LogOperateType.DELETE)
    public ApiResponse<Void> deleteJobs(@RequestBody List<Long> idList) {
        scheduleJobService.deleteJobs(idList);
        return ApiResponse.ok();
    }

    @PutMapping("/run")
    @PreAuthorize("hasAuthority('job:run')")
    @OperateLog(module = "定时任务", name = "执行任务", type = LogOperateType.OTHER)
    public ApiResponse<Void> run(@RequestBody ScheduleJobUpsertDTO request) {
        scheduleJobService.run(request);
        return ApiResponse.ok();
    }

    @PutMapping("/change-status")
    @PreAuthorize("hasAuthority('job:change-status')")
    @OperateLog(module = "定时任务", name = "更改任务状态", type = LogOperateType.OTHER)
    public ApiResponse<Void> changeStatus(@RequestBody ScheduleJobUpsertDTO request) {
        scheduleJobService.changeStatus(request);
        return ApiResponse.ok();
    }

    private void checkBean(String beanName, String methodName) {
        // 检查 Bean 是否有 @Service 注解
        String[] serviceBeans = SpringUtil.getApplicationContext().getBeanNamesForAnnotation(Service.class);
        if (!ArrayUtil.contains(serviceBeans, beanName)) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "只允许指定有@Service注解的Bean!");
        }

        // 检查方法是否存在
        try {
            Object bean = SpringUtil.getBean(beanName);
            Class<?> beanClass = bean.getClass();
            // 获取所有公共方法（包括继承的）
            Method[] methods = beanClass.getMethods();
            boolean methodExists = false;
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    methodExists = true;
                    break;
                }
            }
            if (!methodExists) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        "方法 '" + methodName + "' 在Bean '" + beanName + "' 中不存在!");
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    "无法验证Bean或方法: " + e.getMessage());
        }
    }
}
