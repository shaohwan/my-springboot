package com.demo.daniel.repository;

import com.demo.daniel.model.entity.ScheduleJobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJobLogRepository extends JpaRepository<ScheduleJobLog, Long>, JpaSpecificationExecutor<ScheduleJobLog> {
}
