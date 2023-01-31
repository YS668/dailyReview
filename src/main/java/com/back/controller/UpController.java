package com.back.controller;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.Result;
import com.back.entity.pojo.Reviewdata;
import com.back.entity.pojo.Up;
import com.back.entity.vo.ReviewDataVo;
import com.back.entity.vo.UpVo;
import com.back.service.UpService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 上涨家数 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@RestController
@RequestMapping("/up")
public class UpController {

    @Autowired
    private UpService upService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(upService.list().stream().map(UpVo::of).collect(Collectors.toSet()));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestParam(required = false,defaultValue = "1") int pageNum,
                           @RequestParam(required = false,defaultValue = "10") int pageSize,
                           @RequestParam(required = false) String date){
        LambdaQueryWrapper<Up> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Up> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        //查询条件
        if (date != null){
            wrapper.eq(Up::getRdid,date);
        }

        Page<Up> result = upService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String date){
        LambdaQueryWrapper<Up> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Up::getRdid,date);
        upService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Up up){
        LambdaQueryWrapper<Up> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Up::getRdid,up.getRdid());
        upService.update(up,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Up up){
        upService.save(up);
        return Result.suc();
    }
}
