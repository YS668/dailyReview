package com.back.controller;


import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.QueryPageParam;
import com.back.common.Result;
import com.back.entity.pojo.North;
import com.back.entity.pojo.Reviewdata;
import com.back.entity.pojo.Up;
import com.back.entity.vo.NorthVo;
import com.back.entity.vo.UpVo;
import com.back.service.NorthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 北向资金 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@RestController
@RequestMapping("/north")
public class NorthController {

    @Autowired
    private NorthService northService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(northService.list().stream().map(NorthVo::of).collect(Collectors.toSet()));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<North> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<North> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        String rdid = (String) param.get("rdid");
        //查询条件
        if (rdid != null){
            wrapper.eq(North::getRdid,rdid);
        }

        Page<North> result = northService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String date){
        LambdaQueryWrapper<North> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(North::getRdid,date);
        northService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody North north){
        LambdaQueryWrapper<North> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(North::getRdid,north.getRdid());
        northService.update(north,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody North north){
        northService.save(north);
        return Result.suc();
    }

}
