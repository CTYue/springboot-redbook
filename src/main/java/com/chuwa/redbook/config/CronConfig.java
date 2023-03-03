package com.chuwa.redbook.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
public class CronConfig {
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    public static ThreadPoolTaskScheduler getSpringTaskScheduler() {
        ApplicationContext context = new AnnotationConfigApplicationContext(CronConfig.class);
        return context.getBean(ThreadPoolTaskScheduler.class);
    }

    public static ScheduledExecutorService getScheduledExecutor() {
        return getSpringTaskScheduler().getScheduledExecutor();
    }
}
