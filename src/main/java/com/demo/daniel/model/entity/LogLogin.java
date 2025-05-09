package com.demo.daniel.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_log_login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "ip")
    private String ip;

    @Column(name = "address")
    private String address;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "status")
    private LogLoginStatusType status;

    @Column(name = "operation")
    private LogLoginOperationType operation;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
