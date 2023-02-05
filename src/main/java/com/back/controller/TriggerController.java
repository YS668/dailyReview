package com.back.controller;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.Result;

/**
 * 定时任务控制器
 * 用于后台控制，避开节假日休市
 * 正常情况，周一到周五15.05爬取复盘信息
 */
@RestController
@RequestMapping("/api/trigger")
public class TriggerController {

    //注入任务调度
    @Autowired
    private Scheduler scheduler;

    /**
     * 暂停定时任务
     * @return
     */
    @GetMapping("/pause")
    public Result pauseTrigger(){
        //根据触发器中的withIdentity认证信息对任务进行暂停
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey("craw"));
            return Result.suc();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 恢复定时任务
     * @return
     */
    @GetMapping("/resume")
    public Result resumeTrigger(){
        //根据触发器中的withIdentity认证信息对任务进行暂停
        try {
            scheduler.resumeTrigger(TriggerKey.triggerKey("craw"));
            return Result.suc();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 执行一次定时任务(爬取复盘数据)
     * @return
     */
    @GetMapping("/run/one")
    public Result runOne(){
        //根据触发器中的withIdentity认证信息对任务进行暂停
        try {
            scheduler.triggerJob(JobKey.jobKey("craw"));
            return Result.suc();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.fail();
    }
}
