package com.back.service.impl;

import com.back.entity.pojo.Role;
import com.back.mapper.RoleMapper;
import com.back.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
