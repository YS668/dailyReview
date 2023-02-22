package com.back.service.impl;

import com.back.entity.pojo.User;
import com.back.mapper.UserMapper;
import com.back.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getOneByUserName(String userName) {
        return userMapper.getOneByUserName(userName);
    }

    @Override
    public User getOneByUid(Long uid) {
        return userMapper.getOneByUid(uid);
    }

    @Override
    public int cancelBindByuid(Long uid) {
        return userMapper.cancelBindByuid(uid);
    }
}
