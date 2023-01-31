package com.back.common.constant;

public class WXConstant {

    /** 微信校验token */
    public static final String WX_TOKEN = "leyi668";
    public static final String WX_SIGNATURE = "signature";
    public static final String WX_TIMESTAMP = "timestamp";
    public static final String WX_NONCE = "nonce";
    public static final String WX_ECHOSTR = "echostr";
    public static final String WX_ANSWER_TEXT = "text";
    public static final String WX_TO_USERNAME = "ToUserName";
    public static final String WX_FROM_USERNAME = "FromUserName";
    public static final String WX_MSG_TYPE = "MsgType";
    public static final String WX_CONTENT = "Content";

    public static final String HANDLER_HELP = "帮助";
    public static final String HANDLER_BINDING = "绑定";
    public static final String HANDLER_UNBIND = "解绑";
    public static final String HANDLER_PUSH = "推送";
    public static final String HANDLER_WATCH = "查看";
    public static final String HANDLER_PLAN = "复盘计划";
    public static final String HANDLER_REVIEW_DATA = "复盘数据";

    public static final String HELP_TEXT = "您好，这是我目前的支持的命令：" +"\n"+
            "绑定 用户名" +"\n"+
            "(描述：用空格隔开哦，用于新朋友关联复盘账号！如：绑定 秀)" +"\n"+
            "解绑 用户名" +"\n"+
            "(描述：用空格隔开哦，用于老朋友解绑复盘系统账号！如：解绑 秀）" +"\n"+
            "推送" +"\n"+
            "(描述：请先绑定复盘账号再输入，用于推送自选股行情，包括现价，涨跌，成交额，如：推送）" +"\n"+
            "复盘计划" +"\n"+
            "(描述：请先绑定复盘账号再输入，用于推送复盘计划，如：复盘计划）" +"\n"+
            "查看 股票名称 " +"\n"+
            "(描述：用空格隔开哦，用于推送股股行情，包括现价，涨跌，成交额，如：贵州茅台 宁德时代）";
    public static final String PUSH_TEXT = "股票名称：$" +"\n"+
            "涨跌：$" +"\n"+
            "现价：$" +"\n"+
            "成交额：$" +"\n"+
            "雪球链接：$";
    public static final String WATCH_TEXT = "股票名称：$" +"\n"+
            "涨跌：$" +"\n"+
            "现价：$" +"\n"+
            "成交额：$" +"\n"+
            "雪球链接：$";
    public static final String PLAN_TEXT = "您好，这是$的复盘计划" +"\n"+
            "$"+"\n"+
            "今天记得按计划行事哦";
    public static final String REVIEW_DATA_TEXT = "历史新高数：$" +"\n"+
            "一年新高数：$" +"\n"+
            "一年新低数：$" +"\n"+
            "跌停数：$" +"\n"+
            "跌幅超5%：$" +"\n"+
            "涨停数：$" +"\n"+
            "非一字涨停：$" +"\n"+
            "涨幅超5%：$" +"\n"+
            "上涨家数：$" +"\n"+
            "上证指数：$" +"\n"+
            "创业扳指：$" +"\n"+
            "成交额：$" +"\n"+
            "日期：$";
    public static final String BINDING_TEXT = "您好，绑定成功";
    public static final String UNBIND_TEXT = "您好，解绑成功";

    public static final String WX_FAIL_CONTENT = "抱歉，指令有误，请输入帮助查看指令";
    public static final String WX_NO_WATCH = "抱歉，请输入你想查看的股票名称，并用空格隔开";
    public static final String WX_FAIL_MSG_TYPE = "抱歉，目前支持文字类型的指令";
    public static final String WX_FAIL_BINDING = "抱歉，绑定失败，请确认绑定的用户名是否正确";
    public static final String WX_FAIL_UNBINDING = "抱歉，解绑失败，请确认绑定的用户名是否正确";
    public static final String WX_AGAIN = "抱歉，请稍后重试";
    public static final String WX_NO_BINDING = "抱歉，请先绑定用户";
    public static final String WX_NO_USER = "抱歉，该用户不存在";
    public static final String WX_FAIl_HANDLER = "抱歉，当前该指令出现故障，请联系客服人员，我们将尽力修复";
    public static final String WX_NO_PLAN = "抱歉，当前用户不存在复盘计划";
    public static final String WX_NO_MY_STOCK = "抱歉，当前用户不存在自选股";


}
