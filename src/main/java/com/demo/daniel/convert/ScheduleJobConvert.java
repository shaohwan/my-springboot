package com.demo.daniel.convert;

import com.demo.daniel.model.dto.ScheduleJobUpsertDTO;
import com.demo.daniel.model.entity.ScheduleJob;
import com.demo.daniel.model.vo.ScheduleJobVO;
import org.springframework.beans.BeanUtils;

public class ScheduleJobConvert {

    private ScheduleJobConvert() {
    }

    public static ScheduleJobVO convertToVO(ScheduleJob scheduleJob) {
        ScheduleJobVO scheduleJobVO = new ScheduleJobVO();
        BeanUtils.copyProperties(scheduleJob, scheduleJobVO);
        return scheduleJobVO;
    }

    public static ScheduleJob convertToEntity(ScheduleJobUpsertDTO dto, ScheduleJob scheduleJob, String... ignoreProperties) {
        ScheduleJob newScheduleJob = new ScheduleJob();
        if (scheduleJob != null) {
            newScheduleJob = scheduleJob;
        }
        BeanUtils.copyProperties(dto, newScheduleJob, ignoreProperties);
        return newScheduleJob;
    }
}
