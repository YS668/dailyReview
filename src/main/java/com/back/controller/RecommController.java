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
import com.back.common.utils.DateUtil;
import com.back.entity.pojo.Recomm;
import com.back.entity.pojo.User;
import com.back.entity.vo.RecommVo;
import com.back.service.RecommService;
import com.back.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 建议表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-02-22
 */
@RestController
@RequestMapping("/recomm")
public class RecommController {


    @Autowired
    private RecommService recommService;
    @Autowired
    private UserService userService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(recommService.list().stream().map(RecommVo::of).collect(Collectors.toList()));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<Recomm> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Recomm> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        String rdid = (String) param.get("rdid");
        String userName = (String) param.get("userName");
        User user = userService.getOneByUserName(userName);
        if (user == null){
            return Result.fail("不存在该用户");
        }
        Long uid = user.getUid();
        //查询条件
        if (rdid != null){
            wrapper.eq(Recomm::getRdid,rdid);
        }
        if (uid != null){
            wrapper.eq(Recomm::getUid,uid);
        }
        page.addOrder(OrderItem.desc("rdid"));
        Page<Recomm> result = recommService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords().stream().map(RecommVo::of).collect(Collectors.toList()));
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Recomm recomm){
        recomm.setRdid(DateUtil.getRdid());
        recommService.save(recomm);
        return Result.suc();
    }
}
