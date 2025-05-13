package com.demo.daniel.repository;

import com.demo.daniel.model.entity.LogOperate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogOperateRepository extends JpaRepository<LogOperate, Long>, JpaSpecificationExecutor<LogOperate> {
}
