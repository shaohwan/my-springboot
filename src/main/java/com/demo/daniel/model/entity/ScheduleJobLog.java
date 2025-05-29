package com.demo.daniel.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_job_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJobLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

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

    @Column(name = "status")
    private ScheduleStatus status;

    @Column(name = "error")
    private String error;

    @Column(name = "times")
    private Long times;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
