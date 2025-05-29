package com.demo.daniel.service;

import com.demo.daniel.convert.ScheduleJobConvert;
import com.demo.daniel.model.dto.ScheduleJobQueryDTO;
import com.demo.daniel.model.dto.ScheduleJobUpsertDTO;
import com.demo.daniel.model.entity.ScheduleJob;
import com.demo.daniel.model.entity.ScheduleStatus;
import com.demo.daniel.repository.ScheduleJobRepository;
import com.demo.daniel.util.ScheduleJobSpecifications;
import com.demo.daniel.util.ScheduleUtils;
import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleJobService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.clear();
        List<ScheduleJob> scheduleJobList = scheduleJobRepository.findAll();
        for (ScheduleJob scheduleJob : scheduleJobList) {
            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        }
    }

    public Page<ScheduleJob> getJobs(ScheduleJobQueryDTO request) {
        Specification<ScheduleJob> spec = ScheduleJobSpecifications.buildSpecification(request.getJobName(), request.getJobGroup(), request.getStatus());
        return scheduleJobRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize()));
    }


    public void upsertJob(ScheduleJobUpsertDTO request) {
        ScheduleJob scheduleJob = Optional.ofNullable(request.getId()).flatMap(id -> scheduleJobRepository.findById(id))
                .map(s -> {
                    ScheduleJobConvert.convertToEntity(request, s, "status");
                    ScheduleUtils.updateSchedulerJob(scheduler, s);
                    return s;
                })
                .orElseGet(() -> {
                    ScheduleJob j = ScheduleJobConvert.convertToEntity(request, null);
                    j.setStatus(ScheduleStatus.PAUSE);
                    ScheduleUtils.createScheduleJob(scheduler, j);
                    return j;
                });
        scheduleJobRepository.save(scheduleJob);
    }

    public void deleteJobs(List<Long> idList) {
        for (Long id : idList) {
            scheduleJobRepository.findById(id).ifPresent(scheduleJob -> {
                scheduleJobRepository.deleteById(id);
                ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob);
            });
        }
    }

    public void run(ScheduleJobUpsertDTO request) {
        scheduleJobRepository.findById(request.getId()).ifPresent(scheduleJob -> ScheduleUtils.run(scheduler, scheduleJob));
    }

    public void changeStatus(ScheduleJobUpsertDTO request) {
        scheduleJobRepository.findById(request.getId()).ifPresent(scheduleJob -> {
            scheduleJob.setStatus(request.getStatus());
            scheduleJobRepository.save(scheduleJob);
            if (ScheduleStatus.PAUSE == request.getStatus())
                ScheduleUtils.pauseJob(scheduler, scheduleJob);
            else if (ScheduleStatus.NORMAL == request.getStatus())
                ScheduleUtils.resumeJob(scheduler, scheduleJob);
        });
    }
}
