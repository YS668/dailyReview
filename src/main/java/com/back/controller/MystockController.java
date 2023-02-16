package com.back.controller;


import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.QueryPageParam;
import com.back.common.Result;
import com.back.common.craw.CrawUtil;
import com.back.common.utils.DateUtil;
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
        //uid
        Integer uid = (Integer) param.get("uid");
        //分组
        String groupName = (String) param.get("groupName");
        //名称
        String stockName = (String) param.get("stockName");
        //查询条件
        if (uid != null){
            wrapper.eq(Mystock::getUid,uid);
        }else if (uid == null){
            return Result.fail();
        }
        if (groupName != null && groupName.length() > 0){
            wrapper.eq(Mystock::getGroupName,groupName);
        }
        if (stockName != null && groupName.length() > 0){
            wrapper.eq(Mystock::getStockname,stockName);
        }

        Page<Mystock> result = mystockService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords().stream().map(MyStockVo::of).collect(Collectors.toSet()));
    }

    //删除
    @GetMapping("/del/{stockCode}")
    public Result del(@PathVariable("stockCode") String stockCode){
        LambdaQueryWrapper<Mystock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mystock::getStockcode,stockCode);
        mystockService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Mystock mystock){
        LambdaQueryWrapper<Mystock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mystock::getStockcode,mystock.getStockcode());
        mystockService.update(mystock,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Mystock mystock){
        if (!CrawUtil.StockNameCodeMap.containsKey(mystock.getStockname())){
            return Result.fail("该股票不存在，请重新输入");
        }
        String stockCode = CrawUtil.StockNameCodeMap.get(mystock.getStockname());
        mystock.setStockcode(stockCode);
        mystock.setRdid(DateUtil.getRdid());
        mystockService.save(mystock);
        return Result.suc();
    }

}
