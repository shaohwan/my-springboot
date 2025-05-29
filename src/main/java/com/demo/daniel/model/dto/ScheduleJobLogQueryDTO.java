package com.demo.daniel.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobLogQueryDTO extends PageQuery {

    private Long jobId;

    private String jobName;

    private String jobGroup;
}
