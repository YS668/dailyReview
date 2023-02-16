package com.back.config.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    //指定具体的定时任务类
    @Bean
    public JobDetail uploadTaskDetail() {
        return JobBuilder.newJob(CrawTask.class)
                .withIdentity("craw") //认证信息：根据认证信息来启停定时任务
                .storeDurably().build();
    }

    @Bean
    public Trigger uploadTaskTrigger() {
        //TODO 这里设定执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 5 15 ? * MON-FRI");
        // 返回任务触发器
        return TriggerBuilder.newTrigger().forJob(uploadTaskDetail())
                .withIdentity("craw") //认证信息：根据认证信息来启停定时任务
                .withSchedule(scheduleBuilder)
                .build();
    }
}
