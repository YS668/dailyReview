package com.back.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础pojo类
 */
@Data
public class basePojo implements Serializable {

    @ApiModelProperty(value = "创建人")
    private String createPerson;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updatePerson;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty(value = "删除标识：0有效  1删除")
    private Integer dr;
}
