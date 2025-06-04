package com.demo.daniel.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PositionQueryDTO extends PageQuery {

    private String code;

    private String name;
}
