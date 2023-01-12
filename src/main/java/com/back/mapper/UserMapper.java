package com.back.mapper;

import com.back.entity.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Select("select * from user where userName = #{userName}")
    User getByuserName(@Param("userName") String userName);

    @Select("select * from user where openId = #{openId}")
    User getByOpenId(@Param("openId") String openId);

    @Update("update user set openId = ' ' where uid = #{uid}")
    boolean cancelBindByuid(@Param("uid")Long uid);
}
