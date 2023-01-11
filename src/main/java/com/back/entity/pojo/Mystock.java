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
 * 自选股表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Mystock对象", description="自选股表")
public class Mystock implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号id")
    private Long uid;

    @ApiModelProperty(value = "微信标识")
    @TableField("openId")
    private String openid;

    @ApiModelProperty(value = "股票代码")
    @TableField("stockCode")
    private String stockcode;

    @ApiModelProperty(value = "额外字段")
    private String extra;

    @ApiModelProperty(value = "创建人")
    private String createPerson;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updatePerson;

    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识：0有效  1删除")
    private Integer dr;


}
