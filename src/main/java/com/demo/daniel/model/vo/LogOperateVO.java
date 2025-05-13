package com.demo.daniel.model.vo;

import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.entity.LogStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogOperateVO {

    private Long id;

    private String module;

    private String name;

    private String requestUri;

    private String requestMethod;

    private String requestParameters;

    private String ip;

    private String address;

    private String userAgent;

    private LogOperateType type;

    private LogStatus status;

    private Long duration;

    private String username;

    private String resultMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
