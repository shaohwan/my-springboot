package com.demo.daniel.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobQueryDTO extends PageQuery {

    private String jobName;

    private String jobGroup;

    private Integer status;
}
