package com.demo.daniel.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_job")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJob implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_group")
    private String jobGroup;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "method")
    private String method;

    @Column(name = "params")
    private String params;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "status")
    private ScheduleStatus status;

    @Column(name = "remark")
    private String remark;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
