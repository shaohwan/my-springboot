package com.demo.daniel.repository;

import com.demo.daniel.model.entity.LogLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogLoginRepository extends JpaRepository<LogLogin, Long>, JpaSpecificationExecutor<LogLogin> {
}
