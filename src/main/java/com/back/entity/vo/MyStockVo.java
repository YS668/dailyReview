package com.back.entity.vo;

import com.back.entity.pojo.Mystock;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自选股vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyStockVo {

    @ApiModelProperty(value = "自选日期标识")
    private String rdid;

    @ApiModelProperty(value = "账号id")
    private Long uid;

    @ApiModelProperty(value = "微信标识")
    private String openid;

    @ApiModelProperty(value = "股票代码")
    private String stockcode;

    @ApiModelProperty(value = "股票名称")
    private String stockname;

    @ApiModelProperty(value = "分组")
    private String group;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "额外字段")
    private String extra;

    //vo转换
    public static MyStockVo of(Mystock val){
        MyStockVo vo = new MyStockVo();
        vo.setRdid(val.getRdid());
        vo.setUid(val.getUid());
        vo.setOpenid(val.getOpenid());
        vo.setStockcode(val.getStockcode());
        vo.setStockname(val.getStockname());
        vo.setGroup(val.getGroup());
        vo.setNote(vo.getGroup());
        vo.setExtra(vo.getExtra());
        return vo;
    }
}
