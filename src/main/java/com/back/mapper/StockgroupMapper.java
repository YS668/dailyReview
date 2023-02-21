package com.back.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.back.entity.pojo.Stockgroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 自选分组 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@Mapper
public interface StockgroupMapper extends BaseMapper<Stockgroup> {

    @Select("select groupName from stockgroup where uid = #{uid} and dr = 0")
    List<String> getAllGroup(@Param("uid") Long uid);

    @Select("select groupName from stockgroup where uid = #{uid} and groupName = #{groupName} and dr = 0")
    String getOneByUidGroupName(@Param("uid") Long uid,@Param("groupName") String groupName);
}
