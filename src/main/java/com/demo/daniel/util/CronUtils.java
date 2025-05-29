package com.demo.daniel.util;

import lombok.experimental.UtilityClass;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

@UtilityClass
public class CronUtils {

    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    public static Date getNextExecution(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
