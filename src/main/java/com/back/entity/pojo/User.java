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
 * 账号表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="账号表")
public class User extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号id")
    private Long uid;

    @ApiModelProperty(value = "性别：0男  1女")
    private Integer sex;

    @ApiModelProperty(value = "微信标识")
    @TableField("openId")
    private String openid;

    @ApiModelProperty(value = "会员id")
    @TableField("vipId")
    private Long vipid;

    @ApiModelProperty(value = "用户名")
    @TableField("userName")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "额外字段")
    private String extra;



}
