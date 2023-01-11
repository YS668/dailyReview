package com.back.common.wx.Wxutils;

import com.alibaba.fastjson.JSON;
import com.back.common.BeanUtil;
import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.wx.messagehandler.WxMessageHandler;
import com.back.controller.WX.WxController;
import com.back.entity.pojo.Wxhandler;
import com.back.entity.wx.TextMessage;
import com.back.mapper.WxhandlerMapper;
import com.back.service.WxhandlerService;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private WxhandlerMapper wxhandlerMapper;

    /** bean初始化时注入*/
    public static Map<String, Wxhandler> handMap = new HashMap<>();

    /**
     * 初始化时，注入文本信息
     */
    @PostConstruct
    private void init(){
        List<Wxhandler> list = wxhandlerMapper.getAll();
        list.stream().forEach((wxhandler) -> {
            handMap.put(wxhandler.getKeywords(),wxhandler);
        });
        log.info("初始化的微信指令：{}",JSON.toJSON(handMap));
    }

    /**
     * 把对象转成微信回复需要的xml格式对应的字符串
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
     * @param FromUserName
     *        openId
     * @param ToUserName
     *        开发者微信
     * @param Content
     * @return
     */
    public static String initMessage(String FromUserName, String ToUserName, String Content) {
        TextMessage text = getMessage(FromUserName, ToUserName);
        //分解接收的微信消息
        if (Content == null){
            text.setContent(WXConstant.WX_FAIL_CONTENT);
            return messageToxml(text);
        }
        String[] split = Content.split(" ");
        String channel = null;
        WxMessageHandler handler = null;
        if (split != null && split.length >= CommonConstant.ONE){
            if (split[CommonConstant.ZERO] != null && !split[CommonConstant.ZERO].equals(" ")){
                //存在指令
                switch (split[CommonConstant.ZERO]){
                    //需要进行操作
                    /** 绑定 */
                    case WXConstant.HANDLER_BINDING:
                        /** 解绑 */
                    case WXConstant.HANDLER_UNBIND:
                        /** 推送 */
                    case WXConstant.HANDLER_PUSH:
                        /** 复盘计划 */
                    case WXConstant.HANDLER_PLAN:
                        /** 查看 */
                    case WXConstant.HANDLER_WATCH:
                        channel = handMap.get(WXConstant.HANDLER_HELP).getChannel();
                        handler = (WxMessageHandler)BeanUtil.getBeanByName(channel);
                        text.setContent(handler.handler(FromUserName,Content));
                        break;
                    //无需操作或者指令错误
                    default:
                        if (handMap.containsKey(Content)){
                            text.setContent(handMap.get(Content).getContent());
                        }else {
                            text.setContent(WXConstant.WX_FAIL_CONTENT);
                        }
                        break;
                }
            }else {
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
     * @param content
     * @param fillList
     * @return
     */
    public static String fillMessage(String content, List<String> fillList) {
        String[] split = content.split("\\$");
        StringBuilder builder = new StringBuilder();
        if (split.length == fillList.size() + CommonConstant.ONE){
            for (int i = 0; i < split.length-1; i++) {
                builder.append(split[i]);
                builder.append(fillList.get(i));
            }
            builder.append(split[split.length-1]);
            String res = builder.toString();
            //存在尚未填充的值，需要修复
            if (res.contains("$")){
                return WXConstant.WX_FAIl_HANDLER;
            }
            return builder.toString();
        }else {
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