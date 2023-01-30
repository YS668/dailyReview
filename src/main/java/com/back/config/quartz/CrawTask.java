package com.back.config.quartz;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.back.common.craw.CrawUtil;
import com.back.service.ReviewdataService;

/**
 * 定时任务
 */
public class CrawTask extends QuartzJobBean {

    @Autowired
    private ReviewdataService reviewdataService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //TODO 这里写定时任务的执行逻辑
        CrawUtil.getReviewData();
        System.out.println("简单的定时任务执行时间："+new Date().toLocaleString());
    }
}
