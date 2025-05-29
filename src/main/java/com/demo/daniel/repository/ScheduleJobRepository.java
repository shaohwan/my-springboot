package com.demo.daniel.repository;

import com.demo.daniel.model.entity.ScheduleJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJobRepository extends JpaRepository<ScheduleJob, Long>, JpaSpecificationExecutor<ScheduleJob> {
}
