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
 * 微信回复消息操作表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Wxhandler对象", description="微信回复消息操作表")
public class Wxhandler extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "回复通道")
    private String channel;

    @ApiModelProperty(value = "额外字段")
    private String extra;

}
