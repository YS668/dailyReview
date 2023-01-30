package com.back.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 北向资金 vo类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NorthVo {

    @ApiModelProperty(value = "标识")
    private String rdid;

    @ApiModelProperty(value = "北向净买入")
    private String northAll;

    @ApiModelProperty(value = "上证指数")
    private String shIndex;

    @ApiModelProperty(value = "沪股通")
    private String hgtb;

    @ApiModelProperty(value = "深股通")
    private String sgtb;

    @ApiModelProperty(value = "额外字段")
    private String extra;
}
