package com.demo.daniel.model.vo;

import com.demo.daniel.model.entity.LogLoginOperationType;
import com.demo.daniel.model.entity.LogLoginStatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogLoginVO {

    private Long id;

    private String username;

    private String ip;

    private String address;

    private String userAgent;

    private LogLoginStatusType status;

    private LogLoginOperationType operation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
