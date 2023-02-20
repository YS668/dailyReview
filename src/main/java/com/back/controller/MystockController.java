package com.back.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.back.entity.req.MyStockReq;
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
        if (stockName != null && stockName.length() > 0){
            wrapper.eq(Mystock::getStockname,stockName);
        }

        Page<Mystock> result = mystockService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords().stream().map(MyStockVo::of).collect(Collectors.toSet()));
    }

    //删除自选股
    @PostMapping("/del")
    public Result del(@RequestBody List<MyStockReq> param){
        return mystockService.removeBatch(param);
    }

    //换组
    @PostMapping("/change/group")
    public Result changeGroup(@RequestBody List<MyStockReq> param){
        return mystockService.changeGroup(param);
    }

    //修改备注
    @PostMapping("/update/note")
    public Result updateNote(@RequestBody MyStockReq param){
        return mystockService.updateNote(param);
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Mystock mystock){
        if (!CrawUtil.StockNameCodeMap.containsKey(mystock.getStockname())){
            return Result.fail("该股票不存在，请重新输入");
        }
        //是否存在
        Map<String,String> one = mystockService.getOneByUidStockNameGroupName(mystock.getUid(),
                mystock.getStockname(), mystock.getGroupName());
        if (one != null && one.size() >= 0){
            return Result.fail("该股票已经存在该分组了");
        }
        String stockCode = CrawUtil.StockNameCodeMap.get(mystock.getStockname());
        mystock.setStockcode(stockCode);
        mystock.setRdid(DateUtil.getRdid());
        mystockService.save(mystock);
        return Result.suc();
    }

}
