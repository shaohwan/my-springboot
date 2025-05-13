package com.demo.daniel.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_log_operate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogOperate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "module")
    private String module;

    // 操作名
    @Column(name = "name")
    private String name;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_parameters")
    private String requestParameters;

    @Column(name = "ip")
    private String ip;

    @Column(name = "address")
    private String address;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "type")
    private LogOperateType type;

    @Column(name = "status")
    private LogStatus status;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "username")
    private String username;

    @Column(name = "result_message")
    private String resultMessage;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
