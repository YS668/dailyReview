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
 * 股票表
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Stock对象", description="股票表")
public class Stock extends basePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "股票代码")
    private String code;

    @ApiModelProperty(value = "股票名字")
    @TableField("stockName")
    private String stockname;

    @ApiModelProperty(value = "额外字段")
    private String extra;



}
