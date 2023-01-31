package com.back.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import com.back.common.constant.CrawConstant;
import com.back.common.craw.CrawUtil;

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
    /** 拼音简写*/
    public String stortName;
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

    public StockPushVo(String stockCode, String stockName, String stortName) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stortName = stortName;
    }

    public  String show() {
        return "股票名称：" + stockName+"\n"+
                "涨跌：" + trend+"\n"+
                "现价：" + nowPrice+"\n"+
                "成交额：" + turnover+"\n"+
                "雪球链接：" + xueQiuLink;
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

    /**
     * 爬取上交所股票转换
     * @param map
     * @return
     */
    public static StockPushVo valueofSh(Map<String,String> map){
        return  new StockPushVo(CrawConstant.SH +(String) map.get("val"),
                (String)map.get("val2"),(String)map.get("val3"));
    }

    /**
     * 复盘数据转换
     * @param map
     * @return
     */
    public static StockPushVo of(Map map) {
        Object o = map.get(CrawConstant.STOCK_NAME);
        StockPushVo vo = CrawUtil.StockNameMap.get(map.get(CrawConstant.STOCK_NAME));
        if(vo == null){
            return null;
        }
        return CrawUtil.getOne(vo.getStockCode());
    }


}
