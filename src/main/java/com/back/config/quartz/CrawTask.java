package com.back.config.quartz;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.back.common.constant.CrawConstant;
import com.back.common.craw.CrawUtil;
import com.back.entity.pojo.North;
import com.back.entity.pojo.Reviewdata;
import com.back.entity.pojo.Up;
import com.back.entity.vo.NorthVo;
import com.back.entity.vo.ReviewDataVo;
import com.back.entity.vo.UpVo;
import com.back.service.NorthService;
import com.back.service.ReviewdataService;
import com.back.service.UpService;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务
 */
@Slf4j
public class CrawTask extends QuartzJobBean {

    @Autowired
    private ReviewdataService reviewdataService;
    @Autowired
    private NorthService northService;
    @Autowired
    private UpService upService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始执行定时任务：爬取复盘信息");
        //定时爬取复盘信息
        CrawUtil.getReviewData();
        //复盘数据
        ReviewDataVo reviewDataVo = (ReviewDataVo) CrawUtil.dayReviewMap.get(CrawConstant.REVIEW);
        //北向资金
        NorthVo northVo = (NorthVo) CrawUtil.dayReviewMap.get(CrawConstant.NORTH);
        //上涨家数
        UpVo upVo = (UpVo) CrawUtil.dayReviewMap.get(CrawConstant.UP);
        //保存数据
        reviewdataService.save(Reviewdata.of(reviewDataVo));
        northService.save(North.of(northVo));
        upService.save(Up.of(upVo));
        log.info("定时任务结束：爬取复盘信息");
    }
}
