package com.back.controller;


import java.util.Calendar;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.back.common.Result;
import com.back.entity.pojo.Reviewdata;
import com.back.entity.vo.ReviewDataVo;
import com.back.service.ReviewdataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * <p>
 * 复盘数据表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@RestController
@RequestMapping("/review/data")
public class ReviewdataController {

    @Autowired
    private ReviewdataService reviewdataService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(reviewdataService.list().stream().map(ReviewDataVo::of).collect(Collectors.toSet()));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<Reviewdata> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Reviewdata> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        String rdid = (String) param.get("rdid");
        //查询条件
        if (rdid != null){
            wrapper.eq(Reviewdata::getRdid,rdid);
        }

        Page<Reviewdata> result = reviewdataService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords().stream().map(ReviewDataVo::of).collect(Collectors.toList()));
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String rdid){
        LambdaQueryWrapper<Reviewdata> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reviewdata::getRdid,rdid);
        reviewdataService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Reviewdata reviewdata){
        LambdaQueryWrapper<Reviewdata> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reviewdata::getRdid,reviewdata.getRdid());
        reviewdataService.update(reviewdata,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Reviewdata reviewdata){
        reviewdataService.save(reviewdata);
        return Result.suc();
    }

}
