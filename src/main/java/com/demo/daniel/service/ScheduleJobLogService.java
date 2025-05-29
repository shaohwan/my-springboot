package com.demo.daniel.service;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.ScheduleJobLogQueryDTO;
import com.demo.daniel.model.entity.ScheduleJobLog;
import com.demo.daniel.repository.ScheduleJobLogRepository;
import com.demo.daniel.util.ScheduleJobLogSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobLogService {

    @Autowired
    private ScheduleJobLogRepository scheduleJobLogRepository;

    public void saveLog(ScheduleJobLog scheduleJobLog) {
        scheduleJobLogRepository.save(scheduleJobLog);
    }

    public Page<ScheduleJobLog> getLogs(ScheduleJobLogQueryDTO request) {
        Specification<ScheduleJobLog> spec = ScheduleJobLogSpecifications.buildSpecification(request.getJobId(), request.getJobName(), request.getJobGroup());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return scheduleJobLogRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), sort));
    }

    public ScheduleJobLog getLog(Long id) {
        return scheduleJobLogRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_LOG_NOT_EXIST.getCode(), "Job Log ID " + id + " not found"));
    }
}
