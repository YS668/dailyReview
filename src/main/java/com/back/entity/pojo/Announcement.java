package com.back.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 公告表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Announcement对象", description="公告表")
public class Announcement extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "发公告的人")
    private Long uid;

    @ApiModelProperty(value = "公告编码")
    private Long code;

    @ApiModelProperty(value = "公告主题")
    private Long topic;

    @ApiModelProperty(value = "公告内容")
    private String content;

    @ApiModelProperty(value = "额外字段")
    private String extra;




}
