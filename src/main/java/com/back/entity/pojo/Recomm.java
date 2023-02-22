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
 * 建议表
 * </p>
 *
 * @author DR-back
 * @since 2023-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Recomm对象", description="建议表")
public class Recomm extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户")
    private Long uid;

    @ApiModelProperty(value = "日期标识")
    private String rdid;

    @ApiModelProperty(value = "建议标题")
    private String topic;

    @ApiModelProperty(value = "公告内容")
    private String content;

    @ApiModelProperty(value = "额外字段")
    private String extra;


}
