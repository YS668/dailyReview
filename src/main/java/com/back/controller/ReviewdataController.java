package com.back.controller;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.back.common.Result;
import com.back.entity.vo.ReviewDataVo;
import com.back.service.ReviewdataService;

/**
 * <p>
 * 复盘数据表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-30
 */
@RestController
@RequestMapping("/reviewdata")
public class ReviewdataController {

    @Autowired
    private ReviewdataService reviewdataService;

    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(reviewdataService.list().stream().map(ReviewDataVo::of).collect(Collectors.toSet()));
    }

}
