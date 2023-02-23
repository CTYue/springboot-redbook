package com.chuwa.redbook.cron;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    public void performTask() {

        System.out.println("Regular task performed at "
                + dateFormat.format(new Date()));

    }

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void performDelayedTask() {

        System.out.println("Delayed Regular task performed at "
                + dateFormat.format(new Date()));

    }

    @Scheduled(cron = "*/5 * * * * *")
    public void performTaskUsingCron() {

        System.out.println("Regular task performed using Cron at "
                + dateFormat.format(new Date()));

    }
}
