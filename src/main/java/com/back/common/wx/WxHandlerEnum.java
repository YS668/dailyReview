package com.back.common.wx;

import com.back.common.wx.messagehandler.*;

/**
 * 微信操作枚举类
 */
public enum WxHandlerEnum {
     HELP("帮助", HelpHandler.class),
     BINDING("绑定", BindingHandler.class),
     UNBIND("解绑", UnBindHandler.class),
     PUSH("推送", PushHandler.class),
     WATCH("查看", WatchHandler.class),
     PLAN("复盘计划", ReviewPlanHandler.class),
     REVIEW_DATA("复盘数据", ReviewDataHandler.class),
    ;

    WxHandlerEnum(String name, Class<? extends HandlerAdapter> handlerBean) {
        this.name = name;
        this.handlerBean = handlerBean;
    }
    //输入的指令名字
    private String name;
    //对应的操作类
    private Class<? extends HandlerAdapter> handlerBean;
    //对应的文本内容
    private String text;

    public String getName() {
        return name;
    }

    public Class<? extends HandlerAdapter> getHandlerBean() {
        return handlerBean;
    }

    public String getText() {
        return text;
    }

    public static Class<? extends HandlerAdapter> getHandlerBean(String name) {
        for (WxHandlerEnum value : WxHandlerEnum.values()) {
            if (value.getName().equals(name)){
                return value.getHandlerBean();
            }
        }
        return null;
    }

}
