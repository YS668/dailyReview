package com.back.entity.vo;

import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

import com.back.common.utils.DateUtil;
import com.back.entity.pojo.Reviewdata;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.alibaba.fastjson.JSONObject;



/**
 * 复盘总体数据vo类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDataVo {

    @ApiModelProperty(value = "标识")
    private String rdid;
    /** 创历史新高 <代码，行情>*/
    private Set<StockPushVo> historyHigh;
    /** 创一年新高 <代码，行情>*/
    private Set<StockPushVo> yearHigh;
    /** 创一年新低 <代码，行情>*/
    private Set<StockPushVo> yearLow;
    /** 跌停家数 <代码，行情>*/
    private Set<StockPushVo> downLimit;
    /** 跌幅超5% */
    private Set<StockPushVo> downFive;
    /** 涨停家数 <代码，行情>*/
    private Set<UpLimitVo> upLimit;
    /** 非一字涨停 <代码，行情>*/
    private Set<StockPushVo> noOneUp;
    /** 涨幅超5% */
    private Set<StockPushVo> upFive;
    /** 上涨家数 */
    private int upAll;
    /** 上证指数涨跌 */
    private String SH_INDEX;
    /** 深圳成指涨跌 */
    private String SZ_INDEX;
    /** 创业扳指涨跌 */
    private String Business_INDEX;
    /** 成交额 */
    private String turnOver;
    /** 最高标 */
    private StockPushVo highest;
    /** 今日强力板块前五 */
    private Map<String,StockPushVo> plateFive;


    public String show() {

        return  "日期："+ DateUtil.getByRdid(rdid) + "\n"+
                "今日历史新高数：" + historyHigh.size() + "\n"+
                "今日一年新高数：" + yearHigh.size() + "\n"+
                "今日一年新低数：" + yearLow.size() + "\n"+
                "今日跌停数：" + downLimit.size() + "\n"+
                "今日跌幅超5%：" + downFive.size() + "\n"+
                "今日涨停数：" + upLimit.size() + "\n"+
                "今日非一字涨停：" + noOneUp.size() + "\n"+
                "今日涨幅超5%：" + upFive.size() + "\n"+
                "今日上涨家数：" + upAll + "\n"+
                "上证指数：" + SH_INDEX + "\n"+
                "深证成指：" + SZ_INDEX + "\n"+
                "创业扳指：" + Business_INDEX + "\n"+
                "成交额：" + turnOver;
    }

    //vo转换
    public static ReviewDataVo of(Reviewdata data){
        ReviewDataVo vo = new ReviewDataVo();
        //json转map
        //标识
        vo.setRdid(data.getRdid());
        //历史新高
        vo.setHistoryHigh(JSONObject.parseObject(data.getHistoryHigh(),Set.class));
        //一年新高
        vo.setYearHigh(JSONObject.parseObject(data.getYearHigh(),Set.class));
        //一年新低
        vo.setYearLow(JSONObject.parseObject(data.getYearLow(),Set.class));
        //今日跌停
        vo.setDownLimit(JSONObject.parseObject(data.getDownLimit(),Set.class));
        //今日跌幅超5%
        vo.setDownFive(JSONObject.parseObject(data.getDownFive(),Set.class));
        //今日涨停
        vo.setUpLimit(JSONObject.parseObject(data.getUpLimit(),Set.class));
        //今日非一字涨停
        vo.setNoOneUp(JSONObject.parseObject(data.getNoOneUp(),Set.class));
        //今日涨幅超5%
        vo.setUpFive(JSONObject.parseObject(data.getUpFive(),Set.class));
        //上涨家数
        vo.setUpAll(data.getUpAll());
        //上证指数
        vo.setSH_INDEX(data.getShIndex());
        //深证成指
        vo.setSZ_INDEX(data.getSzIndex());
        //创业扳指
        vo.setBusiness_INDEX(data.getBusinessIndex());
        //成交额
        vo.setTurnOver(data.getTurnOver());
        return vo;
    }

}
