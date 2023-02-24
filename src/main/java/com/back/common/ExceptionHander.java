package com.back.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.back.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;

@RestControllerAdvice
@Component
@Slf4j
public class ExceptionHander {

    //企业微信机器人
    private static String url ="https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=5661e0b8-c8f5-4ef2-8c95-186b32589c38&debug=1";

    @ExceptionHandler(value = Exception.class)
    public Result handler(Exception e){
        log.info("发生异常：{},{}",e.toString(),e.getStackTrace()[0].toString());
        e.printStackTrace();
        //发送企业微信异常
        sendWxException(e);
        return Result.fail();
    }

    public static void sendWxException(Exception e){
        //企业微信推送
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        //文本消息
        params.put("msgtype","text");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("content",DateUtil.getDateByMs(System.currentTimeMillis())+"\n"+e.toString()+"："+e.getStackTrace()[0].toString());
        params.put("text",textMap);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(params, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);
    }
}
