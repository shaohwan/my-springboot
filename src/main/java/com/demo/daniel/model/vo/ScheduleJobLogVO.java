package com.demo.daniel.model.vo;

import com.demo.daniel.model.entity.ScheduleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleJobLogVO {

    private Long id;

    private Long jobId;

    private String jobName;

    private String jobGroup;

    private String beanName;

    private String method;

    private String params;

    private ScheduleStatus status;

    private String error;

    private Long times;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
