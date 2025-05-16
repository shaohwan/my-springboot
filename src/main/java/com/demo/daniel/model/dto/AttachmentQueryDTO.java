package com.demo.daniel.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttachmentQueryDTO extends PageQuery {

    private String name;

    private String platform;
}
