package com.demo.daniel.util;

import cn.hutool.extra.spring.SpringUtil;
import com.demo.daniel.model.entity.ScheduleJob;
import com.demo.daniel.model.entity.ScheduleJobLog;
import com.demo.daniel.model.entity.ScheduleStatus;
import com.demo.daniel.service.ScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public abstract class AbstractScheduleJob implements Job {
    private static final ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        ScheduleJob scheduleJob = new ScheduleJob();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleUtils.JOB_PARAM_KEY), scheduleJob);

        try {
            threadLocal.set(new Date());
            doExecute(scheduleJob);
            saveLog(scheduleJob, null);
        } catch (Exception e) {
            log.error("Job execution failed, job ID: {}", scheduleJob.getId(), e);
            saveLog(scheduleJob, e);
        }
    }

    protected void doExecute(ScheduleJob scheduleJob) throws Exception {
        log.info("Preparing to execute job, job ID: {}", scheduleJob.getId());

        Object bean = SpringUtil.getBean(scheduleJob.getBeanName());
        Method method = bean.getClass().getDeclaredMethod(scheduleJob.getMethod(), String.class);
        method.invoke(bean, scheduleJob.getParams());

        log.info("Job execution finished, job ID: {}", scheduleJob.getId());
    }

    protected void saveLog(ScheduleJob scheduleJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        // 执行总时长
        long times = System.currentTimeMillis() - startTime.getTime();

        // 保存执行记录
        ScheduleJobLog log = new ScheduleJobLog();
        log.setJobId(scheduleJob.getId());
        log.setJobName(scheduleJob.getJobName());
        log.setJobGroup(scheduleJob.getJobGroup());
        log.setBeanName(scheduleJob.getBeanName());
        log.setMethod(scheduleJob.getMethod());
        log.setParams(scheduleJob.getParams());
        log.setTimes(times);
        log.setCreateTime(LocalDateTime.now());

        if (e != null) {
            log.setStatus(ScheduleStatus.PAUSE);
            String error = StringUtils.substring(e.getLocalizedMessage(), 0, 2000);
            log.setError(error);
        } else {
            log.setStatus(ScheduleStatus.NORMAL);
        }

        // 保存日志
        SpringUtil.getBean(ScheduleJobLogService.class).saveLog(log);
    }
}
