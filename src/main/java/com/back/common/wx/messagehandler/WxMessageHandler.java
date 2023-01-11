package com.back.common.wx.messagehandler;

/**
 * 微信操作统一接口
 */
public interface WxMessageHandler {

    /**
     * 微信消息操作
     * @param content
     * @return
     */
    String handler(String openId,String content);
}
