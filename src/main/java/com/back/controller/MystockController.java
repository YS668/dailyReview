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
import com.back.entity.pojo.Mystock;
import com.back.entity.vo.MyStockVo;
import com.back.service.MystockService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 自选股表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@RestController
@RequestMapping("/api/my/stock")
public class MystockController {

    @Autowired
    private MystockService mystockService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(mystockService.list().stream().map(MyStockVo::of).collect(Collectors.toSet()));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<Mystock> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Mystock> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        //分组
        String group = (String) param.get("group");
        //名称
        String stockName = (String) param.get("stockName");
        //查询条件
        if (group != null){
            wrapper.eq(Mystock::getGroup,group);
        }
        if (stockName != null){
            wrapper.eq(Mystock::getStockname,stockName);
        }

        Page<Mystock> result = mystockService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords().stream().map(MyStockVo::of).collect(Collectors.toSet()));
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String rdid){
        LambdaQueryWrapper<Mystock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mystock::getRdid,rdid);
        mystockService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Mystock mystock){
        LambdaQueryWrapper<Mystock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mystock::getRdid,mystock.getRdid());
        mystockService.update(mystock,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Mystock mystock){
        mystockService.save(mystock);
        return Result.suc();
    }

}
