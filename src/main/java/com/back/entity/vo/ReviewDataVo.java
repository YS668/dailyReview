package com.back.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
    private Map<String,StockPushVo> historyHigh;
    /** 创一年新高 <代码，行情>*/
    private Map<String,StockPushVo> yearHigh;
    /** 创一年新低 <代码，行情>*/
    private Map<String,StockPushVo> yearLow;
    /** 跌停家数 <代码，行情>*/
    private Map<String,StockPushVo> downLimit;
    /** 跌幅超5% */
    private Map<String,StockPushVo> downFive;
    /** 涨停家数 <代码，行情>*/
    private Map<String,StockPushVo> upLimit;
    /** 非一字涨停 <代码，行情>*/
    private Map<String,StockPushVo> noOneUp;
    /** 涨幅超5% */
    private Map<String,StockPushVo> upFive;
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
        return  "今日历史新高数：" + historyHigh.size() + "\n"+
                "今日一年新高数：" + yearHigh.size() + "\n"+
                "今日一年新低数：" + yearLow.size() + "\n"+
                "今日跌停数：" + downLimit.size() + "\n"+
                "今日跌幅超5%：" + downFive + "\n"+
                "今日涨停数：" + upLimit.size() + "\n"+
                "今日非一字涨停：" + noOneUp.size() + "\n"+
                "今日涨幅超5%：" + upFive + "\n"+
                "今日上涨家数：" + upAll + "\n"+
                "上证指数：" + SH_INDEX + "\n"+
                "上证指数：" + SZ_INDEX + "\n"+
                "创业扳指：" + Business_INDEX + "\n"+
                "成交额：" + turnOver + '\n' +
                "最高标：" + highest + "\n"+
                "板块涨幅前五：" + plateFive + "\n";
    }


}
