package com.back.common.utils.Wxutils;

import com.back.common.constant.WXConstant;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 验证微信服务器token接入
 */
@Slf4j
public class EchostrCheckUtil {

    /**
     * 开发者通过检验signature对请求进行校验（下面有校验方式）。
     * 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，
     * 否则接入失败。加密/校验流程如下：
     * 1）将token、timestamp、nonce三个参数进行字典序排序
     * 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    public static String checkSignature(HttpServletRequest request) {
        //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
        String signature = request.getParameter(WXConstant.WX_SIGNATURE);
        //时间戳
        String timestamp = request.getParameter(WXConstant.WX_TIMESTAMP);
        //随机数
        String nonce = request.getParameter(WXConstant.WX_NONCE);
        //随机字符串
        String echostr = request.getParameter(WXConstant.WX_ECHOSTR);
        //打印日志
        log.info("校验token，signature：{}，timestamp：{}，nonce：{}，echostr：{}",signature,timestamp,nonce,echostr);
        String[] str = new String[]{WXConstant.WX_TOKEN, timestamp, nonce};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.getSHA1(buffer.toString());
        log.info("sha1加密temp：{}",temp);
        //与微信提供的signature进行匹对
        if ( signature!= null && signature.equals(temp)) {
            return echostr;
        } else {
            return null;
        }
    }
}