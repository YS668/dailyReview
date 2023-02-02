package com.back.entity.vo;

import com.back.entity.pojo.ReviewPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPlanVo  {

    @ApiModelProperty(value = "标识")
    private String rdid;

    @ApiModelProperty(value = "复盘用户")
    private Long uid;

    @ApiModelProperty(value = "微信标识")
    private String openId;

    @ApiModelProperty(value = "复盘主题")
    private Long topic;

    @ApiModelProperty(value = "复盘类型：0日复盘  1周复盘 2月复盘 3年复盘")
    private Integer type;

    @ApiModelProperty(value = "模式标识：0指数板块个股个人 1全文")
    private Integer flag;

    @ApiModelProperty(value = "复盘内容（0.JSON格式）")
    private String content;

    @ApiModelProperty(value = "额外字段")
    private String extra;

    //vo转换
    public static ReviewPlanVo of( ReviewPlan data) {
        ReviewPlanVo vo = new ReviewPlanVo();
        vo.setRdid(data.getRdid());
        vo.setUid(data.getUid());
        vo.setTopic(data.getTopic());
        vo.setOpenId(data.getOpenId());
        vo.setFlag(data.getFlag());
        vo.setType(data.getType());
        vo.setContent(data.getContent());
        vo.setExtra(data.getExtra());
        return vo;
    }

    //展示
    public String show(String userName){
        return "您好，这是"+userName+"的复盘计划" +"\n"+
                content+"\n"+
                "今天记得按计划行事哦";
    }
}
