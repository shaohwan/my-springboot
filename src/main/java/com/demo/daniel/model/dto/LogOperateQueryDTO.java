package com.demo.daniel.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogOperateQueryDTO extends PageQuery {

    private String username;

    private String module;

    private String requestUri;

    private Integer status;
}
