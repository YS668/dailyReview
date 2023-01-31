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
 * 解除绑定
 */
@Component
public class UnBindHandler extends HandlerAdapter {

    @Resource
    private UserMapper userMapper;

    @Override
    public String handler(String openId,String content) {
        String[] split = content.split(" ");
        if (split.length != CommonConstant.TWO){
            return WXConstant.WX_FAIL_CONTENT;
        }
        //开始解绑
        String userName= split[CommonConstant.ONE];
        User user = userMapper.getByuserName(userName);
        //用户不存在
        if (user == null){
            return WXConstant.WX_NO_USER;
        }
        //解绑
        if (user.getOpenid() != null && user.getOpenid().equals(openId)){
            //解绑操作
            if (userMapper.cancelBindByuid(user.getUid()) > CommonConstant.ZERO){
                return WXConstant.UNBIND_TEXT;
            }else {
                return WXConstant.WX_AGAIN;
            }

        //不存在绑定关系
        }else {
            return WXConstant.WX_FAIL_UNBINDING;
        }
    }

}
