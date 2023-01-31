package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.wx.Wxutils.TextMessageUtil;
import com.back.entity.pojo.User;
import com.back.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 微信绑定
 */
@Component("BindingHandler")
public class BindingHandler extends HandlerAdapter {

    @Resource
    private UserMapper userMapper;

    @Override
    public String handler(String openID,String content) {
        String[] split = content.split(" ");
        //输入错误
        if (split.length != CommonConstant.TWO ){
            return WXConstant.WX_FAIL_CONTENT;
        }
        //开始绑定
        String userName= split[CommonConstant.ONE];
        User user = userMapper.getByuserName(userName);
        //用户不存在
        if (user == null){
            return WXConstant.WX_NO_USER;
        }
        //未绑定
        if (user.getOpenid() == null || user.getOpenid().equals(" ") ){
            //开始绑定
            user.setOpenid(openID);
            if (userMapper.updateById(user) > CommonConstant.ZERO){
                return  WXConstant.BINDING_TEXT;
            }else {
                return WXConstant.WX_AGAIN;
            }
        //账号已经绑定过
        }else {
            return WXConstant.WX_FAIL_BINDING;
        }
    }
}
