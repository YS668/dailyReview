package com.back.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.back.common.Result;
import com.back.common.constant.CrawConstant;
import com.back.common.craw.CrawUtil;
import com.back.common.utils.DateUtil;
import com.back.common.utils.RedisUtil;
import com.back.entity.vo.BaseStockVo;
import com.back.entity.vo.StockPushVo;
import com.back.entity.vo.UpLimitVo;

/**
 * 定时任务控制器
 * 用于后台控制，避开节假日休市
 * 正常情况，周一到周五15.05爬取复盘信息
 */
@RestController
@RequestMapping("/api")
public class CrawController {

    //注入任务调度
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 暂停定时任务
     * @return
     */
    @GetMapping("/trigger/pause")
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
    @GetMapping("/trigger/resume")
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
    @GetMapping("/trigger/run/one")
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

    /**
     * 获取涨停数据
     * @return
     */
    @GetMapping("/up/limit")
    public Result getUpLimit(){
        return Result.suc(CrawUtil.vo.getUpLimit());
    }

    /**
     * 获取热股榜
     */
    @GetMapping("/hot")
    public Result getHot(){
        Map<String, Object> map= new HashMap<>();
        //雪球1小时内热榜
        List<StockPushVo> oneXq = CrawUtil.getHotByXueQiu(CrawConstant.HOT_ONE_XUEQIU);
        //雪球24小时内热榜
        List<StockPushVo> tfXq = CrawUtil.getHotByXueQiu(CrawConstant.HOT_TF_XUEQIU);
        //淘股吧热榜
        Map<String, List<StockPushVo>> taoGuMap = CrawUtil.getHotByTaoGu();
        //东方财富人气榜
        List<StockPushVo> dfRq = CrawUtil.getHotByDf(CrawConstant.HOT_DF_RENQI);
        //东方财富飙升榜
        List<StockPushVo> dfUp = CrawUtil.getHotByDf(CrawConstant.HOT_DF_UP);
        //同花顺1小时内热榜
        List<StockPushVo> thOne = CrawUtil.getHotByTh(CrawConstant.HOT_ONE_TH);
        //同花顺24小时内热榜
        List<StockPushVo> thTfUp = CrawUtil.getHotByTh(CrawConstant.HOT_TF_TH);
        map.put("xq_one",oneXq);
        map.put("xq_tf",tfXq);
        map.put("tg_sh",taoGuMap.get("sh"));
        map.put("tg_sz",taoGuMap.get("sz"));
        map.put("df_rq",dfRq);
        map.put("df_up",dfUp);
        map.put("th_one",thOne);
        map.put("th_tf",thTfUp);
        return Result.suc(map);
    }

    /**
     * 主要指数拥挤度
     * @return
     */
    @GetMapping("/index/percentage")
    public Result getIndexPercentage(){
        List res = JSONObject.parseObject(RedisUtil.getString("indexPercentage"), List.class);
        if (res == null){
            return Result.suc(CrawUtil.getIndexPercentage());
        }
        return Result.suc(res);
    }

    /**
     * 成交额排行
     * @return
     */
    @GetMapping("/turnover/sort")
    public Result genTurnOverSort(){
        List res = JSONObject.parseObject(RedisUtil.getString("turnOverSort"), List.class);
        if (res == null){
            return Result.suc(CrawUtil.getTurnOverSort());
        }
        return Result.suc(res);
    }

    /**
     * 行业板块成交额排行
     * @return
     */
    @GetMapping("/industry/sort")
    public Result getIndustrySort(){
        List res = JSONObject.parseObject(RedisUtil.getString("industrySort"), List.class);
        if (res == null){
            return Result.suc(CrawUtil.getIndustrySort());
        }
        return Result.suc(res);
    }

    /**
     * 概念板块成交额排行
     * @return
     */
    @GetMapping("/concept/sort")
    public Result getConceptSort(){
        List res = JSONObject.parseObject(RedisUtil.getString("conceptSort"), List.class);
        if (res == null){
            return Result.suc(CrawUtil.getConceptSort());
        }
        return Result.suc(res);
    }

    /**
     * 爬取微信文章
     */
    @GetMapping("/wx/article")
    public Result getWxArticle(){
        return Result.suc(CrawUtil.getWxArticle());
    }
}
