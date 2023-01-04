package com.back.controller.WX;

import com.back.common.utils.Wxutils.MessageUtil;
import com.back.common.utils.Wxutils.TextMessageUtil;
import com.back.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/wx")
    public String answerMessage(HttpServletRequest request, HttpServletResponse response){
        return messageService.answerMessage(request,response);
    }

}
