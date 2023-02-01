package com.back.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.Result;
import com.back.entity.pojo.User;
import com.back.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 账号表 前端控制器
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //获取所有数据
    @GetMapping("/all")
    public Result getAll(){
        return Result.suc(userService.list());
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestParam(required = false,defaultValue = "1") int pageNum,
                           @RequestParam(required = false,defaultValue = "10") int pageSize,
                           @RequestBody(required = false) Map<String,String> map){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        //查询条件：性别、用户名、邮箱、手机号
        String sex = map.get("sex");
        String username = map.get("username");
        String mail = map.get("mail");
        String phone = map.get("phone");
        if (sex != null){
            wrapper.eq(User::getSex,sex);
        }
        if (username != null){
            wrapper.eq(User::getUsername,username);
        }
        if (mail != null){
            wrapper.eq(User::getMail,mail);
        }
        if (phone != null){
            wrapper.eq(User::getPhone,phone);
        }

        Page<User> result = userService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String uid){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUid,uid);
        userService.remove(wrapper);
        return Result.suc();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUid,user.getUid());
        userService.update(user,wrapper);
        return Result.suc();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        userService.save(user);
        return Result.suc();
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> map){
        return Result.suc();
    }

    //登出
    @GetMapping("/logout")
    public Result logout(@RequestParam String uid){
        return Result.suc();
    }
}
