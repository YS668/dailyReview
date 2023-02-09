package com.back.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseStockVo {

    /** 股票代码 */
    private String stockCode ;
    /** 股票名称 */
    private String stockName ;
    /** 涨跌 */
    private String trend ;
    /** 现价 */
    private String  nowPrice;

    public static BaseStockVo of(StockPushVo put){
        BaseStockVo vo = new BaseStockVo();
        vo.setStockCode(put.getStockCode());
        vo.setStockName(put.getStockName());
        vo.setNowPrice(put.getNowPrice());
        vo.setTrend(put.getTrend());
        return vo;
    }
}
