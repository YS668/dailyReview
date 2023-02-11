package com.back.entity.vo;

import com.back.common.constant.CommonConstant;
import com.back.common.utils.CodeUtil;
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
public class StockPushVo extends BaseStockVo  {

    @ApiModelProperty(value = "标识")
    private String rdid;

    /** 成交额 */
    private String  turnover;
    /** 占比 */
    private String percentage;
    /** 雪球链接 */
    private String  xueQiuLink;
    /** 同花顺链接 */
    private String  tongHLink;
    /** 淘股吧链接 */
    private String  taoGuLink;
    /** 东方财富链接 */
    private String dongFangLink;

    public StockPushVo(String rdid, String stockCode, String stockName, String trend, String nowPrice, String turnover, String xueQiuLink, String tongHLink, String taoGuLink, String dongFangLink) {
        super(stockCode, stockName, trend, nowPrice);
        this.rdid = rdid;
        this.turnover = turnover;
        this.xueQiuLink = xueQiuLink;
        this.tongHLink = tongHLink;
        this.taoGuLink = taoGuLink;
        this.dongFangLink = dongFangLink;
    }

    public StockPushVo(String rdid, String turnover, String xueQiuLink, String tongHLink, String taoGuLink, String dongFangLink) {
        this.rdid = rdid;
        this.turnover = turnover;
        this.xueQiuLink = xueQiuLink;
        this.tongHLink = tongHLink;
        this.taoGuLink = taoGuLink;
        this.dongFangLink = dongFangLink;
    }

    public StockPushVo(String stockCode) {
        this.setStockCode(stockCode);
    }

    public StockPushVo(String stockCode, String stockName) {
        this.setStockCode(stockCode);
        this.setStockName(stockName);
    }

    public  String show() {
        return  "日期："+ DateUtil.getFlmat() + "\n"+
                "股票名称：" + this.getStockName()+"\n"+
                "涨跌：" + this.getTrend()+"\n"+
                "现价：" + this.getNowPrice()+"\n"+
                "成交额：" + turnover+"\n"+
                "雪球链接：" + xueQiuLink+"\n"+
                "淘股吧链接：" + taoGuLink+"\n"+
                "同花顺链接：" + tongHLink+"\n"+
                "东方财富链接：" + dongFangLink;
    }

    /**
     * 爬取上交所股票转换
     * @param map
     * @return
     */
    public static StockPushVo valueofSh(Map<String,String> map){
        return  new StockPushVo(CrawConstant.SH +(String) map.get("val"),
                (String)map.get("val2"));
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
        StockPushVo res = CrawUtil.getOneBySinA(stockCode);
        return res;
    }

    /**
     * 填充链接
     */
    public void fillLink() {
        //雪球链接
        //https://xueqiu.com/S/SZ000821
        this.setXueQiuLink(CrawConstant.XUE_QIU_ONE+this.getStockCode());
        //淘股吧链接
        //https://www.taoguba.com.cn/quotes/sz000821
        this.setTaoGuLink(CrawConstant.TAO_GU_ONE+ CodeUtil.toLow(this.getStockCode()));
        //东方财富
        //https://so.eastmoney.com/web/s?keyword=%E4%BA%AC%E5%B1%B1%E8%BD%BB%E6%9C%BA
        this.setDongFangLink(CrawConstant.DONG_FANG_ONE+this.getStockCode());
        //同花顺
        //http://stockpage.10jqka.com.cn/600519
        this.setTongHLink(CrawConstant.TONG_HU_ONE+CodeUtil.codeToNum(this.getStockCode()));
    }


}
