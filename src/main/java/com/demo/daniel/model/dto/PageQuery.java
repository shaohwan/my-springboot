package com.demo.daniel.model.dto;

import lombok.Data;

@Data
public class PageQuery {

    private int page = 0;

    private int size = 10;
}
