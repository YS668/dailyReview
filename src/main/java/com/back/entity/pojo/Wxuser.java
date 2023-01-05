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
 * 微信用户信息表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Wxuser对象", description="微信用户信息表")
public class Wxuser extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "微信标识")
    @TableField("openId")
    private String openid;

    @ApiModelProperty(value = "额外字段")
    private String extra;


}
