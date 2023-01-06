package com.back.controller;


import com.back.common.Result;
import com.back.entity.pojo.Wxhandler;
import com.back.service.WxhandlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 微信回复消息操作表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/wxhandler")
public class WxhandlerController {

    @Resource
    private WxhandlerService wxhandlerService;

    @PostMapping("/add")
    public Result add(Wxhandler wxhandler){
        return wxhandlerService.add(wxhandler);
    }

    @PostMapping("/update")
    public Result update(Wxhandler wxhandler){
        return wxhandlerService.updateByKeyWords(wxhandler);
    }

}
