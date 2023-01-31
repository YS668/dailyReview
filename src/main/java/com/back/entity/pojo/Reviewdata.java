package com.back.entity.pojo;

import com.alibaba.fastjson.JSON;
import com.back.entity.vo.ReviewDataVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 复盘数据表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Reviewdata对象", description="复盘数据表")
public class Reviewdata extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**以日期为标记，如2023-1-30，则为20230130*/
    @ApiModelProperty(value = "标识")
    private String rdid;

    @ApiModelProperty(value = "历史新高")
    private String historyHigh;

    @ApiModelProperty(value = "一年新高")
    private String yearHigh;

    @ApiModelProperty(value = "一年新低")
    private String yearLow;

    @ApiModelProperty(value = "跌停数")
    private String downLimit;

    @ApiModelProperty(value = "跌幅超5%")
    private String downFive;

    @ApiModelProperty(value = "涨停数")
    private String upLimit;

    @ApiModelProperty(value = "非一字涨停")
    private String noOneUp;

    @ApiModelProperty(value = "涨幅超5%")
    private String upFive;

    @ApiModelProperty(value = "上涨家数")
    private Integer upAll;

    @ApiModelProperty(value = "上证指数")
    private String shIndex;

    @ApiModelProperty(value = "深证成指")
    private String szIndex;

    @ApiModelProperty(value = "创业扳指")
    private String businessIndex;

    @ApiModelProperty(value = "成交额")
    private String turnOver;

    @ApiModelProperty(value = "额外字段")
    private String extra;

    //vo转换
    public static Reviewdata of(ReviewDataVo vo){
        Reviewdata data = new Reviewdata();
        //map转json
        //标识
        data.setRdid(vo.getRdid());
        //历史新高
        data.setHistoryHigh(JSON.toJSON(vo.getHistoryHigh()).toString());
        //一年新高
        data.setYearHigh(JSON.toJSON(vo.getYearHigh()).toString());
        //一年新低
        data.setYearLow(JSON.toJSON(vo.getYearLow()).toString());
        //今日跌停
        data.setDownLimit(JSON.toJSON(vo.getDownLimit()).toString());
        //今日跌幅超5%
        data.setDownFive(JSON.toJSON(vo.getDownFive()).toString());
        //今日涨停
        data.setUpLimit(JSON.toJSON(vo.getUpLimit()).toString());
        //今日非一字涨停
        data.setNoOneUp(JSON.toJSON(vo.getNoOneUp()).toString());
        //今日涨幅超5%
        data.setUpFive(JSON.toJSON(vo.getUpFive()).toString());
        //上涨家数
        data.setUpAll(vo.getUpAll());
        //上证指数
        data.setShIndex(vo.getSH_INDEX());
        //深证成指
        data.setSzIndex(vo.getSZ_INDEX());
        //创业扳指
        data.setBusinessIndex(vo.getBusiness_INDEX());
        //成交额
        data.setTurnOver(vo.getTurnOver());
        return data;
    }

}
