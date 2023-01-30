package com.back.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上涨家数 vo类
 */
@Data
public class UpVo {

	@ApiModelProperty(value = "标识")
	private String rdid;

	@ApiModelProperty(value = "9.30上涨家数")
	private Integer ninetfup;

	@ApiModelProperty(value = "10.00上涨家数")
	private Integer tenup;

	@ApiModelProperty(value = "11.00上涨家数")
	private Integer elevenup;

	@ApiModelProperty(value = "13.00上涨家数")
	private Integer thirteenup;

	@ApiModelProperty(value = "14.00上涨家数")
	private Integer fourteenup;

	@ApiModelProperty(value = "14.30上涨家数")
	private Integer fourteentheup;

	@ApiModelProperty(value = "15.00上涨家数")
	private Integer fifteenup;
}
