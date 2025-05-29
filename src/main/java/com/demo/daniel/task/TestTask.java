package com.demo.daniel.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestTask {

    public void run(String params) throws InterruptedException {
        log.info("我是testTask.run(), 参数: {}, 正在被执行.", params);
        Thread.sleep(1000);
    }
}
