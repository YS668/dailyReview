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
 * 自选分组
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Stockgroup对象", description="自选分组")
public class Stockgroup extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "自选日期标识")
    private String rdid;

    @ApiModelProperty(value = "账号id")
    private Long uid;

    @ApiModelProperty(value = "微信标识")
    @TableField("openId")
    private String openid;

    @ApiModelProperty(value = "分组名称")
    @TableField("groupName")
    private String groupname;

    @ApiModelProperty(value = "额外字段")
    private String extra;


}
