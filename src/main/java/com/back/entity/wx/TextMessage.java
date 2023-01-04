package com.back.entity.wx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本消息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage extends BaseMessage{

    /**文本消息内容*/
    private String Content;
    /**消息id，64位整型*/
    private String MsgId;


}
