package com.back.mapper;

import com.back.entity.pojo.Mystock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 自选股表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-01-07
 */
@Mapper
public interface MystockMapper extends BaseMapper<Mystock> {

    @Select("select * from mystock where openId = #{openId}")
     List<Mystock> getByOpenId(@Param("openId") String openId);
}
