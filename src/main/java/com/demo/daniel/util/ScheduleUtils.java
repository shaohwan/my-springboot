package com.demo.daniel.util;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.entity.ScheduleJob;
import com.demo.daniel.model.entity.ScheduleStatus;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Objects;

@UtilityClass
@Slf4j
public class ScheduleUtils {

    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";
    private final static String JOB_NAME = "TASK_NAME_";

    public static TriggerKey getTriggerKey(ScheduleJob scheduleJob) {
        return TriggerKey.triggerKey(JOB_NAME + scheduleJob.getId(), scheduleJob.getJobGroup());
    }

    public static JobKey getJobKey(ScheduleJob scheduleJob) {
        return JobKey.jobKey(JOB_NAME + scheduleJob.getId(), scheduleJob.getJobGroup());
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            // job key
            JobKey jobKey = getJobKey(scheduleJob);
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleConcurrentExecution.class).withIdentity(jobKey).build();

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob))
                    .withSchedule(scheduleBuilder).build();

            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(JOB_PARAM_KEY, scheduleJob);
            // 把任务添加到Quartz中
            scheduler.scheduleJob(jobDetail, trigger);

            // 判断是否存在
            if (scheduler.checkExists(jobKey)) {
                // 防止创建时存在数据问题，先移除，然后再执行创建操作
                scheduler.deleteJob(jobKey);
            }

            // 判断任务是否过期
            if (!Objects.isNull(CronUtils.getNextExecution(scheduleJob.getCronExpression()))) {
                // 执行调度任务
                scheduler.scheduleJob(jobDetail, trigger);
            }

            // 暂停任务
            if (scheduleJob.getStatus() == ScheduleStatus.PAUSE) {
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "创建定时任务失败 " + e.getLocalizedMessage());
        }
    }


    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(JOB_PARAM_KEY, scheduleJob);

            JobKey jobKey = getJobKey(scheduleJob);
            if (scheduler.checkExists(jobKey)) {
                scheduler.triggerJob(jobKey, dataMap);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "执行定时任务失败 " + e.getLocalizedMessage());
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.pauseJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "暂停定时任务失败 " + e.getLocalizedMessage());
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.resumeJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "恢复定时任务失败 " + e.getLocalizedMessage());
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateSchedulerJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(scheduleJob);

        try {
            // 防止创建时存在数据问题，先移除，然后再执行创建操作
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "更新定时任务失败 " + e.getLocalizedMessage());
        }

        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.deleteJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "删除定时任务失败 " + e.getLocalizedMessage());
        }
    }
}
