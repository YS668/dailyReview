package com.back.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 复盘数据vo类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDataVo {

    /** 创历史新高 <代码，行情>*/
    private Map<String,StockPushVo> historyHigh;
    /** 创一年新高 <代码，行情>*/
    private Map<String,StockPushVo> yearHigh;
    /** 创一年新低 <代码，行情>*/
    private Map<String,StockPushVo> yearLow;
    /** 跌停家数 <代码，行情>*/
    private Map<String,StockPushVo> downLimit;
    /** 跌幅超5% */
    private int downFive;
    /** 涨停家数 <代码，行情>*/
    private Map<String,StockPushVo> upLimit;
    /** 非一字涨停 <代码，行情>*/
    private Map<String,StockPushVo> noOneUp;
    /** 涨幅超5% */
    private int upFive;
    /** 上涨家数 */
    private int upAll;
    /** 上证指数涨跌 */
    private BigDecimal SZ_INDEX;
    /** 创业扳指涨跌 */
    private BigDecimal Business_INDEX;
    /** 成交额 */
    private String turnOver;
    /** 最高标 */
    private StockPushVo highest;
    /** 今日强力板块前五 */
    private Map<String,StockPushVo> plateFive;
}
