package com.back.entity.vo;

import java.util.Objects;

import com.back.common.craw.CrawUtil;
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
public class MyStockVo extends StockPushVo {

    @ApiModelProperty(value = "自选日期标识")
    private String rdid;

    @ApiModelProperty(value = "账号id")
    private Long uid;

    @ApiModelProperty(value = "微信标识")
    private String openid;

    @ApiModelProperty(value = "分组")
    private String groupName;

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
        vo.setStockCode(val.getStockcode());
        vo.setStockName(val.getStockname());
        vo.setGroupName(val.getGroupName());
        vo.setNote(val.getNote());
        vo.setExtra(val.getExtra());
        StockPushVo temp = CrawUtil.getOneBySinA(vo.getStockCode());
        vo.setNowPrice(temp.getNowPrice());
        vo.setTrend(temp.getTrend());
        vo.setTurnover(temp.getTurnover());
        vo.fillLink();
        return vo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyStockVo that = (MyStockVo) o;
        //股票名称及分组及用户一致既相等
        return this.getStockName().equals(that.getStockName()) && groupName.equals(that.groupName) && uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getStockName(), groupName,uid);
    }
}
