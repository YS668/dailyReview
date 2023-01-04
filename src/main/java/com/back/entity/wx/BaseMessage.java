package com.back.entity.wx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信消息基类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage {

    /**开发者微信*/
    private String ToUserName;
    /**发送方账号（一个OpenID）*/
    private String FromUserName;
    /**消息创建时间整型*/
    private long CreateTime;
    /**消息类型*/
    private String MsgType;


}
