package com.back.mapper;


import com.back.entity.pojo.ReviewPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 复盘表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Mapper
public interface ReviewPlanMapper extends BaseMapper<ReviewPlan> {

    @Select("select * from reviewplan where openId = #{openId} ord")
    List<ReviewPlan> getByOpenId(@Param("openId") String openId);
}
