package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.wx.Wxutils.TextMessageUtil;
import com.back.entity.pojo.ReviewPlan;
import com.back.entity.pojo.User;
import com.back.mapper.ReviewPlanMapper;
import com.back.mapper.ReviewPlanMapper;
import com.back.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推送复盘计划
 */
@Component
public class ReviewPlanHandler extends HandlerAdapter {

    @Resource
    private ReviewPlanMapper reviewPlanMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public String handler(String openId,String content) {
        if (content.equals(WXConstant.HANDLER_PLAN)){
            //回复模板，进行填充
            String resContent = WXConstant.PLAN_TEXT;
            List<ReviewPlan> eos = reviewPlanMapper.getByOpenId(openId);
            //0代表日复盘
            List<ReviewPlan> cl = eos.stream().filter((rp -> rp.getType() == CommonConstant.ZERO)).collect(Collectors.toList());
            //目标记录，最近的一条复盘记录
            if (cl.size() != 0){
                List<String> fillList = new ArrayList<>();
                //用户名
                User user = userMapper.getByOpenId(openId);
                //没有绑定
                if(user == null){
                    return WXConstant.WX_NO_BINDING;
                }
                fillList.add(user.getUsername());
                //复盘计划
                fillList.add(cl.get(CommonConstant.ZERO).getContent());
                return TextMessageUtil.fillMessage(resContent,fillList);
            }
            //没找到日复盘
            return WXConstant.WX_NO_PLAN;
        }else {
            return WXConstant.WX_FAIL_CONTENT;
        }
    }

}
