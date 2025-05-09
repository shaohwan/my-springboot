package com.demo.daniel.convert;

import com.demo.daniel.model.entity.LogLogin;
import com.demo.daniel.model.vo.LogLoginVO;
import org.springframework.beans.BeanUtils;

public class LogLoginConvert {

    private LogLoginConvert() {
    }

    public static LogLoginVO convertToVO(LogLogin logLogin) {
        LogLoginVO logLoginVO = new LogLoginVO();
        BeanUtils.copyProperties(logLogin, logLoginVO);
        return logLoginVO;
    }
}
