package com.back.entity.vo;

import java.math.BigDecimal;
import java.util.Map;

import com.back.common.constant.CrawConstant;
import com.back.common.utils.DateUtil;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpLimitVo extends StockPushVo{

    /**最新价*/
    //a股市值(不含限售股)
    private String value;
    //几天几板
    private String dayNum;
    //连续涨停天数
    private Integer day;
    //涨停原因类别
    private String reason;
    //涨停封单量
    private String upNum;
    //涨停封单额
    private String upValue;
    //涨停封单量占流通a股比
    private BigDecimal circulationPercentage;
    //涨停封单量占成交量比
    private BigDecimal allPercentage;
    //涨停开板次数
    private String openNum;
    //涨停类型
    private String upType;
    //首次涨停时间
    private String firstUp;
    //最终涨停时间
    private String lastUp;

    public UpLimitVo(StockPushVo sp){
        super(sp.getRdid(),sp.getStockCode(),sp.getStockName(),
                sp.getStortName(),sp.getTrend(),sp.getNowPrice(),sp.getTurnover(),
                sp.getXueQiuLink(),sp.getTongHLink(),sp.getTaoGuLink(),sp.getDongFangLink());
    }

    public static UpLimitVo ofUp(Map map) {
        UpLimitVo vo = new UpLimitVo(StockPushVo.of(map));
        String rdid = DateUtil.getRdid();
        vo.setValue((String) map.get(CrawConstant.VALUE+"["+rdid+"]"));
        vo.setDay((Integer) map.get(CrawConstant.DAY+"["+rdid+"]"));
        vo.setDayNum((String) map.get(CrawConstant.DAY_NUM+"["+rdid+"]"));
        vo.setReason((String) map.get(CrawConstant.REASON+"["+rdid+"]"));
        vo.setUpNum((String) map.get(CrawConstant.UP_NUM+"["+rdid+"]"));
        vo.setUpValue((String) map.get(CrawConstant.UP_VALUE+"["+rdid+"]"));
        vo.setCirculationPercentage((BigDecimal) map.get(CrawConstant.CIRCULATION_PERCENTAGE+"["+rdid+"]"));
        vo.setAllPercentage((BigDecimal) map.get(CrawConstant.ALL_PERCENTAGE+"["+rdid+"]"));
        vo.setOpenNum((String) map.get(CrawConstant.OPEN_NUM+"["+rdid+"]"));
        vo.setUpType((String) map.get(CrawConstant.UP_TYPE+"["+rdid+"]"));
        vo.setFirstUp((String) map.get(CrawConstant.FIRST_UP+"["+rdid+"]"));
        vo.setLastUp((String) map.get(CrawConstant.LAST_UP+"["+rdid+"]"));
        return vo;
    }

    //转变为父类
    public static StockPushVo toSuper(UpLimitVo vo){
        StockPushVo res = new StockPushVo();
        res.setRdid(vo.getRdid());
        /** 股票代码 */
        res.setStockCode(vo.getStockCode());
        /** 股票名称 */
        res.setStockName(vo.getStockName());
        /** 拼音简写*/
        res.setStortName(vo.getStortName());
        /** 涨跌 */
        res.setTrend(vo.getTrend());
        /** 现价 */
        res.setNowPrice(vo.getNowPrice());
        /** 成交额 */
        res.setTurnover(vo.getTurnover());
        /** 雪球链接 */
        res.setXueQiuLink(vo.getXueQiuLink());
        /** 同花顺链接 */
        res.setTongHLink(vo.getTongHLink());
        /** 淘股吧链接 */
        res.setTaoGuLink(vo.getTaoGuLink());
        /** 东方财富链接 */
        res.setDongFangLink(vo.getDongFangLink());
        return res;
    }
}
