package com.back.common.wx.messagehandler;

import com.back.common.constant.WXConstant;
import org.springframework.stereotype.Component;

@Component
public class HelpHandler extends HandlerAdapter{

    @Override
    public String handler(String openId, String content) {
        return WXConstant.HELP_TEXT;
    }
}
