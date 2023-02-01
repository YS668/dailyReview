package com.back.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.Result;
import com.back.entity.pojo.User;
import com.back.entity.pojo.Vip;
import com.back.service.VipService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/vip")
public class VipController {

    @Autowired
    private VipService vipService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(vipService.list());
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestParam(required = false,defaultValue = "1") int pageNum,
                           @RequestParam(required = false,defaultValue = "10") int pageSize,
                           @RequestBody(required = false) Map<String,String> map){
        LambdaQueryWrapper<Vip> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Vip> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        //查询条件：有效期
        String expired = map.get("expired");
        if (expired != null){
            wrapper.eq(Vip::getExpired,expired);
        }

        Page<Vip> result = vipService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String vipId){
        LambdaQueryWrapper<Vip> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vip::getVipid,vipId);
        vipService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Vip vip){
        LambdaQueryWrapper<Vip> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vip::getVipid,vip.getVipid());
        vipService.update(vip,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Vip vip){
        vipService.save(vip);
        return Result.suc();
    }

}
