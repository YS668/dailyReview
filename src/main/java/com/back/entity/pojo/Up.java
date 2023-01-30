package com.back.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 上涨家数
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Up对象", description="上涨家数")
public class Up  extends basePojo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标识")
    private String rdid;

    @ApiModelProperty(value = "9.25上涨家数")
    @TableField("nineTfUp")
    private Integer ninetfup;

    @ApiModelProperty(value = "10.00上涨家数")
    @TableField("tenUp")
    private Integer tenup;

    @ApiModelProperty(value = "11.00上涨家数")
    @TableField("elevenUp")
    private Integer elevenup;

    @ApiModelProperty(value = "13.00上涨家数")
    @TableField("thirteenUp")
    private Integer thirteenup;

    @ApiModelProperty(value = "14.00上涨家数")
    @TableField("fourteenUp")
    private Integer fourteenup;

    @ApiModelProperty(value = "14.30上涨家数")
    @TableField("fourteenTheUp")
    private Integer fourteentheup;

    @ApiModelProperty(value = "15.00上涨家数")
    @TableField("fifteenUp")
    private Integer fifteenup;

    @ApiModelProperty(value = "额外字段")
    private String extra;


}
