package com.back.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.back.common.QueryPageParam;
import com.back.common.Result;
import com.back.common.constant.CommonConstant;
import com.back.entity.pojo.User;
import com.back.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
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
@RequestMapping("/api/user")
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
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();

        //查询条件：性别、用户名、邮箱、手机号
        String sex = (String) param.get("sex");
        String username = (String) param.get("username");
        String mail = (String) param.get("mail");
        String phone = (String) param.get("phone");
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
        page.addOrder(OrderItem.desc("rdid"));
        Page<User> result = userService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam Integer uid){
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
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //账号密码
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        if (username != null){
            wrapper.eq(User::getUsername,username);
        }
        if (password != null){
            wrapper.eq(User::getPassword,password);
        }
        List<User> list = userService.list(wrapper);
        if (list.isEmpty()){
            return  Result.fail();
        }
        return Result.suc(list.get(CommonConstant.ZERO));
    }

    //登出
    @GetMapping("/logout")
    public Result logout(@RequestParam Integer uid){
        return Result.suc();
    }
}
