package com.back.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.QueryPageParam;
import com.back.common.Result;
import com.back.entity.pojo.Announcement;
import com.back.entity.pojo.Vip;
import com.back.service.AnnouncementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(announcementService.list());
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Announcement> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();

        //查询条件：公告编码、发公告的人、主题、时间
        String code = (String) param.get("code");
        Integer uid = (Integer) param.get("uid");
        String topic = (String) param.get("topic");
        String time = (String) param.get("time");
        if (code != null){
            wrapper.eq(Announcement::getCode,code);
        }
        if (uid != null){
            wrapper.eq(Announcement::getUid,uid);
        }
        if (time != null){
            wrapper.gt(Announcement::getCreateTime,time);
        }
        if (code != null){
            wrapper.eq(Announcement::getCode,code);
        }

        Page<Announcement> result = announcementService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String code){
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getCode,code);
        announcementService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Announcement announcement){
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getCode,announcement.getCode());
        announcementService.update(announcement,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Announcement announcement){
        announcementService.save(announcement);
        return Result.suc();
    }
}
