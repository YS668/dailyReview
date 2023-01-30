package com.back.entity.pojo;

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

}
