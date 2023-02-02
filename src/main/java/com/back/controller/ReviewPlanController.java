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
import com.back.entity.pojo.ReviewPlan;
import com.back.entity.vo.ReviewPlanVo;
import com.back.service.ReviewPlanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 复盘计划表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/review/plan")
public class ReviewPlanController {

    @Autowired
    private ReviewPlanService reviewPlanService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(reviewPlanService.list().stream().map(ReviewPlanVo::of).collect(Collectors.toList()));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<ReviewPlan> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<ReviewPlan> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        String rdid = (String) param.get("rdid");
        //查询条件
        if (rdid != null){
            wrapper.eq(ReviewPlan::getRdid,rdid);
        }

        Page<ReviewPlan> result = reviewPlanService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords().stream().map(ReviewPlanVo::of).collect(Collectors.toList()));
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String rdid){
        LambdaQueryWrapper<ReviewPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewPlan::getRdid,rdid);
        reviewPlanService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody ReviewPlan plan){
        LambdaQueryWrapper<ReviewPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewPlan::getRdid,plan.getRdid());
        reviewPlanService.update(plan,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody ReviewPlan plan){
        reviewPlanService.save(plan);
        return Result.suc();
    }
}
