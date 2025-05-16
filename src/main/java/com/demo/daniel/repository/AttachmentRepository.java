package com.demo.daniel.repository;

import com.demo.daniel.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment> {
}
