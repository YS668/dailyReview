package com.back.common.wx.Wxutils;

import com.alibaba.fastjson.JSON;
import com.back.common.BeanUtil;
import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.wx.WxHandlerEnum;
import com.back.common.wx.messagehandler.HandlerAdapter;
import com.back.entity.wx.TextMessage;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回复微信文本消息工具类
 */
@Slf4j
@Component("textMessageUtil")
public class TextMessageUtil {

    /**
     * spring自动注入
     */
    public static Map<String, HandlerAdapter> handMap = new HashMap<>();

    /**
     * 初始化时，注入文本信息
     */
//    @PostConstruct
//    private void init() {
//        log.info("初始化的微信指令：{}", JSON.toJSON(handMap));
//    }

    /**
     * 把对象转成微信回复需要的xml格式对应的字符串
     *
     * @param message
     * @return
     */
    private static String messageToxml(TextMessage message) {
        XStream xstream = new XStream();
        xstream.alias("xml", message.getClass());
        return xstream.toXML(message);
    }

    /**
     * 初始化一条回复信息
     *
     * @param FromUserName openId
     * @param ToUserName   开发者微信
     * @param Content
     * @return
     */
    public static String initMessage(String FromUserName, String ToUserName, String Content) {
        TextMessage text = getMessage(FromUserName, ToUserName);
        //分解接收的微信消息
        if (Content == null) {
            text.setContent(WXConstant.WX_FAIL_CONTENT);
            return messageToxml(text);
        }
        String[] split = Content.split(" ");
        String channel = null;
        HandlerAdapter handler = null;
        if (split != null && split.length >= CommonConstant.ONE) {
            if (split[CommonConstant.ZERO] != null && !split[CommonConstant.ZERO].equals(" ")) {
                //存在指令
                String str = split[CommonConstant.ZERO];
                //找适配器
                Class<? extends HandlerAdapter> handlerBean = WxHandlerEnum.getHandlerBean(str);
                if (handlerBean != null) {
                    handler = (HandlerAdapter) BeanUtil.getBeanByName(handlerBean.getSimpleName());
                    text.setContent(handler.handler(FromUserName, Content));
                }
            } else {
                text.setContent(WXConstant.WX_FAIL_CONTENT);
            }
        } else {
            text.setContent(WXConstant.WX_FAIL_CONTENT);
        }
        log.info("回复的微信消息：{}", JSON.toJSON(text));
        return messageToxml(text);
    }

    /**
     * 接收非文本消息的回复
     *
     * @param fromUserName
     * @param toUserName
     * @return
     */
    public static String failMsgType(String fromUserName, String toUserName) {
        TextMessage text = getMessage(fromUserName, toUserName);
        text.setContent(WXConstant.WX_FAIL_MSG_TYPE);
        return messageToxml(text);
    }

    /**
     * 回复消息基础封装
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    private static TextMessage getMessage(String FromUserName, String ToUserName) {
        TextMessage text = new TextMessage();
        text.setToUserName(FromUserName);
        text.setFromUserName(ToUserName);
        text.setCreateTime(System.currentTimeMillis());
        text.setMsgType(WXConstant.WX_ANSWER_TEXT);
        return text;
    }

    /**
     * 消息填充类，填充{}
     *
     * @param content
     * @param fillList
     * @return
     */
    public static String fillMessage(String content, List<String> fillList) {
        String[] split = content.split("\\$");
        StringBuilder builder = new StringBuilder();
        if (split.length == fillList.size() ) {
            for (int i = 0; i < split.length; i++) {
                builder.append(split[i]);
                builder.append(fillList.get(i));
            }
            String res = builder.toString();
            //存在尚未填充的值，需要修复
            if (res.contains("$")) {
                return WXConstant.WX_FAIl_HANDLER;
            }
            return builder.toString();
        } else {
            return WXConstant.WX_FAIl_HANDLER;
        }
    }

    public static void main(String[] args) {
        String str = "您好，这是$的复盘计划\n" +
                "$\n" +
                "今天记得按计划行事哦";
        System.out.println(str.contains("$"));
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("test");
        String res = fillMessage(str, list);
        System.out.println(res);
        System.out.println(res.contains("$"));
    }
}