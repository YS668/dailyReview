package com.back.controller;

import com.back.common.utils.EchostrCheckUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController()
public class WxMessageController {

    @GetMapping("/wx")
    public String login(HttpServletRequest request){
        return EchostrCheckUtil.checkSignature(request);
    }
    @GetMapping("/{param}")
    public String get(@PathVariable String param){
        return param;
    }
}
