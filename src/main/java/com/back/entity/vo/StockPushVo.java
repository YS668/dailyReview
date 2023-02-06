package com.back.entity.vo;

import com.back.common.constant.CommonConstant;
import com.back.common.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;
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

    @ApiModelProperty(value = "标识")
    private String rdid;
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
    /** 同花顺链接 */
    private String  TongHLink;
    /** 淘股吧链接 */
    private String  taoGuLink;
    /** 东方财富链接 */
    private String dongFangLink;


    public StockPushVo(String stockCode) {
        this.stockCode = stockCode;
    }

    public StockPushVo(String stockCode, String stockName, String stortName) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stortName = stortName;
    }

    public  String show() {
        return  "日期："+ DateUtil.getFlmat() + "\n"+
                "股票名称：" + stockName+"\n"+
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
     * 解析为复盘数据
     * @param map
     * @return
     */
    public static StockPushVo of(Map map) {
        String stockName = (String) map.get(CrawConstant.STOCK_NAME);
        String temp =(String) map.get(CrawConstant.STOCK_CODE);
        String stockCode = temp.substring(CommonConstant.SEVEN)+temp.substring(CommonConstant.ZERO,CommonConstant.SIX);
        StockPushVo tempVo = CrawUtil.StockCodeMap.get(stockName);
        if(tempVo == null){
            tempVo = new StockPushVo();
            tempVo.setStockName(stockName);
            tempVo.setStockCode(stockCode);
            CrawUtil.StockNameCodeMap.put(stockName,stockCode);
            CrawUtil.StockCodeMap.put(stockCode,tempVo);
        }
        StockPushVo res = CrawUtil.getOne(stockCode);
        //雪球链接
        //https://xueqiu.com/S/SZ000821
        res.setXueQiuLink(CrawConstant.XUE_QIU_ONE+stockCode);
        //淘股吧链接
        //https://www.taoguba.com.cn/quotes/sz000821
        res.setTaoGuLink(CrawConstant.TAO_GU_ONE+temp.substring(CommonConstant.SEVEN).toLowerCase()
                +temp.substring(CommonConstant.ZERO,CommonConstant.SIX));
        //东方财富
        //https://so.eastmoney.com/web/s?keyword=%E4%BA%AC%E5%B1%B1%E8%BD%BB%E6%9C%BA
        res.setDongFangLink(CrawConstant.DONG_FANG_ONE+stockName);
        //同花顺
        //http://www.iwencai.com/unifiedwap/result?w=京山轻机
        res.setTongHLink(CrawConstant.TONG_HU_ONE+stockName);
        res.setRdid(DateUtil.getRdid());
        return res;
    }


}
