package com.back.service.Impl;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.utils.Wxutils.MessageUtil;
import com.back.common.utils.Wxutils.TextMessageUtil;
import com.back.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 回复微信消息
     * @param request
     * @param response
     * @return
     */
    @Override
    public String answerMessage(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(CommonConstant.CHARACTER_ENCODE);
        //将微信请求xml转为map格式，获取所需的参数
        Map<String,String> map = MessageUtil.xmlToMap(request);
        log.info("接受的微信消息：{}",map);
        //开发者微信号
        String ToUserName = map.get(WXConstant.WX_TO_USERNAME);
        // 发送方帐号（一个OpenID）
        String FromUserName = map.get(WXConstant.WX_FROM_USERNAME);
        //消息类型，文本为text
        String MsgType = map.get(WXConstant.WX_MSG_TYPE);
        //文本消息内容
        String Content = map.get(WXConstant.WX_CONTENT);
        //处理文本类型，实现输入回复相应的封装的内容
        if(WXConstant.WX_ANSWER_TEXT.equals(MsgType)){
            //回复文字消息
            return TextMessageUtil.initMessage(FromUserName, ToUserName,Content);
        }else {
            //无法处理
            return TextMessageUtil.failMessage(FromUserName, ToUserName);
        }
    }
}
