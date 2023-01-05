package com.back.mapper;

import com.back.entity.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 账号表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
