package com.back.entity.vo;

import java.math.BigDecimal;
import java.util.Map;

import com.back.common.constant.CrawConstant;
import com.back.common.utils.DateUtil;
import com.back.common.utils.MathUtil;
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
        super(sp.getRdid(),sp.getStockCode(),sp.getStockName(),sp.getTrend(),sp.getNowPrice(),sp.getTurnover(),
                sp.getXueQiuLink(),sp.getTongHLink(),sp.getTaoGuLink(),sp.getDongFangLink());
    }

    public static UpLimitVo ofUp(Map map) {
        UpLimitVo vo = new UpLimitVo(StockPushVo.of(map));
        String rdid = DateUtil.getRdid();
        //判空处理
        Object value = map.get(CrawConstant.VALUE+"["+rdid+"]");
        if (value != null){
            vo.setValue(MathUtil.formatNum(value.toString(),false));
        }
        Object day = map.get(CrawConstant.DAY + "[" + rdid + "]");
        if (day != null){
            vo.setDay((Integer) day);
        }
        Object dayNum = (map.get(CrawConstant.DAY_NUM+"["+rdid+"]"));
        if (dayNum != null){
            vo.setDayNum(dayNum.toString());
        }
        Object reason = map.get(CrawConstant.REASON + "[" + rdid + "]");
        if (reason != null){
            vo.setReason(reason.toString());
        }
        Object upNum = map.get(CrawConstant.UP_NUM + "[" + rdid + "]");
        if (upNum != null){
            vo.setUpNum(upNum.toString());
        }
        Object upValue = map.get(CrawConstant.UP_VALUE + "[" + rdid + "]");
        if (upValue != null){
            vo.setUpValue(MathUtil.formatNum(upValue.toString(),false));
        }
        Object circulationPercentage = map.get(CrawConstant.CIRCULATION_PERCENTAGE + "[" + rdid + "]");
        if (circulationPercentage != null){
            vo.setCirculationPercentage((BigDecimal) circulationPercentage);
        }
        Object allPercentageo = map.get(CrawConstant.ALL_PERCENTAGE + "[" + rdid + "]");
        if (allPercentageo != null){
            vo.setAllPercentage((BigDecimal) allPercentageo);
        }
        Object openNum = map.get(CrawConstant.OPEN_NUM + "[" + rdid + "]");
        if (openNum != null){
            vo.setOpenNum(openNum.toString());
        }
        Object upType = map.get(CrawConstant.UP_TYPE + "[" + rdid + "]");
        if (upType != null){
            vo.setUpType(upType.toString());
        }
        Object firstUp = map.get(CrawConstant.FIRST_UP + "[" + rdid + "]");
        if (firstUp != null){
            vo.setFirstUp(firstUp.toString());
        }
        Object lastUp = map.get(CrawConstant.LAST_UP + "[" + rdid + "]");
        if (lastUp != null){
            vo.setLastUp(lastUp.toString());
        }
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
