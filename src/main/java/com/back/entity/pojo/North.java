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
 * 北向资金
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="North对象", description="北向资金")
public class North extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
