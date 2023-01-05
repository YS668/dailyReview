package com.back.common.utils.Wxutils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.back.common.constant.WXConstant;
import com.back.entity.wx.TextMessage;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextMessageUtil {

    //把对象转成微信回复需要的xml格式对应的字符串

    private static String messageToxml(TextMessage message) {
        XStream xstream = new XStream();
        xstream.alias("xml", message.getClass());
        return xstream.toXML(message);
    }

    //普通消息
    private static String initMessage(String FromUserName, String ToUserName) {
        return null;
    }

    public static String initMessage(String FromUserName, String ToUserName, String Content) {
        TextMessage text = new TextMessage();
        text.setToUserName(FromUserName);
        text.setFromUserName(ToUserName);
        text.setContent("您输入的内容是：" + Content);
        text.setCreateTime(System.currentTimeMillis());
        text.setMsgType(WXConstant.WX_ANSWER_TEXT);
        log.info("回复的微信消息：{}", JSON.toJSON(text));
        return messageToxml(text);
    }

    public static String failMessage(String FromUserName, String ToUserName) {
        TextMessage text = new TextMessage();
        text.setToUserName(FromUserName);
        text.setFromUserName(ToUserName);
        text.setContent(WXConstant.WX_FAIL_ANSWER);
        text.setCreateTime(System.currentTimeMillis());
        text.setMsgType(WXConstant.WX_ANSWER_TEXT);
        return messageToxml(text);
    }
}