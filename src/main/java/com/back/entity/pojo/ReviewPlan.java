package com.back.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 复盘表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="reviewplan对象", description="复盘表")
@TableName(value = "reviewplan")
public class ReviewPlan extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "复盘用户")
    private Long uid;

    @ApiModelProperty(value = "微信标识")
    private String openId;

    @ApiModelProperty(value = "复盘主题")
    private Long topic;

    @ApiModelProperty(value = "复盘类型：0日复盘  1周复盘 2月复盘 3年复盘")
    private Integer type;

    @ApiModelProperty(value = "模式标识：0指数板块个股个人 1全文")
    private Integer flag;

    @ApiModelProperty(value = "复盘内容（0.JSON格式）")
    private String content;

    @ApiModelProperty(value = "额外字段")
    private String extra;



}
