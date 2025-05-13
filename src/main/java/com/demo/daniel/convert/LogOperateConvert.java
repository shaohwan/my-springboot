package com.demo.daniel.convert;

import com.demo.daniel.model.entity.LogOperate;
import com.demo.daniel.model.vo.LogOperateVO;
import org.springframework.beans.BeanUtils;

public class LogOperateConvert {

    private LogOperateConvert() {
    }

    public static LogOperateVO convertToVO(LogOperate logOperate) {
        LogOperateVO logOperateVO = new LogOperateVO();
        BeanUtils.copyProperties(logOperate, logOperateVO);
        return logOperateVO;
    }
}
