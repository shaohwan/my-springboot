package com.demo.daniel.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogLoginQueryDTO extends PageQuery {

    private String username;

    private String startTime;

    private String endTime;
}
