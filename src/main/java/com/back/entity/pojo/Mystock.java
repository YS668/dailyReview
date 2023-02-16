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
 * @since 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Mystock对象", description="自选股表")
public class Mystock extends basePojo implements Serializable {

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

    @ApiModelProperty(value = "股票代码")
    @TableField("stockCode")
    private String stockcode;

    @ApiModelProperty(value = "股票名称")
    @TableField("stockName")
    private String stockname;

    @ApiModelProperty(value = "分组")
    @TableField("groupName")
    private String groupName;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "额外字段")
    private String extra;


}
