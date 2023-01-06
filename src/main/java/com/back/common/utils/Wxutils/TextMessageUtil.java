package com.back.common.utils.Wxutils;

import com.alibaba.fastjson.JSON;
import com.back.common.constant.WXConstant;
import com.back.entity.pojo.Wxhandler;
import com.back.entity.wx.TextMessage;
import com.back.mapper.WxhandlerMapper;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TextMessageUtil {

    @Resource
    private WxhandlerMapper wxhandlerMapper;

    /** bean初始化时注入*/
    public static Map<String, Wxhandler> handMap = new HashMap<>();

    @PostConstruct
    private void init(){
        List<Wxhandler> list = wxhandlerMapper.getAll();
        list.stream().forEach((wxhandler) -> {
            handMap.put(wxhandler.getKeywords(),wxhandler);
        });
        log.info("初始化的微信指令：{}",JSON.toJSON(handMap));
    }

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
        TextMessage text = getMessage(FromUserName, ToUserName);
        if (handMap.containsKey(Content)){
            text.setContent(handMap.get(Content).getContent());
        }else {
            text.setContent(WXConstant.WX_FAIL_CONTENT);
        }
        log.info("回复的微信消息：{}", JSON.toJSON(text));
        return messageToxml(text);
    }

    public static String failMessage(String FromUserName, String ToUserName) {
        TextMessage text = getMessage(FromUserName, ToUserName);
        text.setContent(WXConstant.WX_FAIL_CONTENT);
        return messageToxml(text);
    }

    public static TextMessage getMessage(String FromUserName, String ToUserName) {
        TextMessage text = new TextMessage();
        text.setToUserName(FromUserName);
        text.setFromUserName(ToUserName);
        text.setCreateTime(System.currentTimeMillis());
        text.setMsgType(WXConstant.WX_ANSWER_TEXT);
        return text;
    }
}