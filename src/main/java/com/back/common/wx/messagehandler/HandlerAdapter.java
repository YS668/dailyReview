package com.back.common.wx.messagehandler;

/**
 * 适配器模式
 */
public abstract class HandlerAdapter {

    /**
     * 微信消息操作
     * @param content
     * @return
     */
    public abstract String handler(String openId, String content);
}
