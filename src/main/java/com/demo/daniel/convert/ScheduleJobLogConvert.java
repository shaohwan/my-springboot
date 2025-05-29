package com.demo.daniel.convert;

import com.demo.daniel.model.entity.ScheduleJobLog;
import com.demo.daniel.model.vo.ScheduleJobLogVO;
import org.springframework.beans.BeanUtils;

public class ScheduleJobLogConvert {

    private ScheduleJobLogConvert() {
    }

    public static ScheduleJobLogVO convertToVO(ScheduleJobLog scheduleJobLog) {
        ScheduleJobLogVO scheduleJobLogVO = new ScheduleJobLogVO();
        BeanUtils.copyProperties(scheduleJobLog, scheduleJobLogVO);
        return scheduleJobLogVO;
    }
}
