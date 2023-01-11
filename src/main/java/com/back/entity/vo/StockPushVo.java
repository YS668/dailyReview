package com.back.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 股票行情VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPushVo  {

    /** 股票代码 */
    private String stockCode ;
    /** 股票名称 */
    private String stockName ;
    /** 涨跌 */
    private String trend ;
    /** 现价 */
    private String  nowPrice;
    /** 成交额 */
    private String  turnover;
    /** 雪球链接 */
    private String  xueQiuLink;

    public StockPushVo(String stockCode) {
        this.stockCode = stockCode;
    }

    @Override
    public String toString() {
        return "StockPushVo{" +
                "stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", trend='" + trend + '\'' +
                ", nowPrice='" + nowPrice + '\'' +
                ", turnover='" + turnover + '\'' +
                ", xueQiuLink='" + xueQiuLink + '\'' +
                '}';
    }
}
