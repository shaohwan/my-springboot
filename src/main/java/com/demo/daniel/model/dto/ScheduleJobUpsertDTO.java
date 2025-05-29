package com.demo.daniel.model.dto;

import com.demo.daniel.model.entity.ScheduleStatus;
import lombok.Data;

@Data
public class ScheduleJobUpsertDTO {

    private Long id;

    private String jobName;

    private String jobGroup;

    private String beanName;

    private String method;

    private String params;

    private String cronExpression;

    private ScheduleStatus status;

    private String remark;
}
