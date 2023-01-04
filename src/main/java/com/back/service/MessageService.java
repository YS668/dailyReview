package com.back.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MessageService {


    /**
     * 回复微信消息
     * @param request
     * @param response
     * @return
     */
    String answerMessage(HttpServletRequest request, HttpServletResponse response);
}
