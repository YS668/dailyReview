package com.back.controller;


import com.back.common.Result;
import com.back.entity.pojo.Wxhandler;
import com.back.service.WxhandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/del")
    public Result del(String keyWords){
        return wxhandlerService.del(keyWords);
    }

    @GetMapping("/get/page")
    public Result getPage(@RequestParam(required = false,value = "keywords") String keywords,
                          @RequestParam(required = false,value = "channel") String channel,
                          @RequestParam(required = false,value = "pageNum",defaultValue = "1") int pageNum,
                          @RequestParam(required = false,value = "pageSize",defaultValue = "10") int pageSize){
        return wxhandlerService.getPage(keywords,channel,pageNum,pageSize);
    }

    @GetMapping("/get/all")
    public Result getAll(){
        return wxhandlerService.getAll();
    }

}
