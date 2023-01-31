package com.back.entity.vo;

import com.back.entity.pojo.North;
import com.back.entity.pojo.Up;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上涨家数 vo类
 */
@Data
public class UpVo {

	@ApiModelProperty(value = "标识")
	private String rdid;

	@ApiModelProperty(value = "9.25上涨家数")
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

	//vo转换
	public static UpVo of(Up up){
		UpVo vo = new UpVo();
		//标识
		vo.setRdid(up.getRdid());
		//9.25上涨家数
		vo.setNinetfup(up.getNinetfup());
		//10.00上涨家数
		vo.setTenup(up.getTenup());
		//11.00上涨家数
		vo.setElevenup(up.getElevenup());
		//13.00上涨家数
		vo.setThirteenup(up.getThirteenup());
		//14.00上涨家数
		vo.setFourteenup(up.getFourteenup());
		//14.30上涨家数
		vo.setFourteentheup(up.getFourteentheup());
		//15.00上涨家数
		vo.setFifteenup(up.getFifteenup());
		return vo;
	}
}
