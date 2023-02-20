package com.back.controller;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.back.common.Result;
import com.back.common.utils.DateUtil;
import com.back.entity.pojo.Mystock;
import com.back.entity.pojo.Stockgroup;
import com.back.service.StockgroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * <p>
 * 自选分组 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@RestController
@RequestMapping("/api/stock/group")
public class StockGroupController {

    @Autowired
    private StockgroupService stockgroupService;

    //获取所有分组
    @GetMapping("/all/{uid}")
    public Result getAll(@PathVariable("uid") Long uid){
        List<String> res = stockgroupService.getAllGroup(uid);
        return Result.suc(res);
    }

    //新建分组
    @PostMapping ("/save")
    public Result save(@RequestBody Stockgroup param){
        //是否存在
        String one = stockgroupService.getOneByUidGroupName(param.getUid(), param.getGroupname());
        if (one != null && one.length() > 0){
            return Result.fail("该分组已经存在了");
        }
        param.setRdid(DateUtil.getRdid());
        stockgroupService.save(param);
        return Result.suc();
    }

    //删除分组
    @PostMapping ("/del/{uid}/{groupName}")
    public Result del(@PathVariable("uid") Integer uid,@PathVariable("groupName") String groupName){

        LambdaQueryWrapper<Stockgroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Stockgroup::getGroupname,groupName);
        wrapper.eq(Stockgroup::getUid,uid);
        stockgroupService.remove(wrapper);
        return Result.suc();
    }

}
