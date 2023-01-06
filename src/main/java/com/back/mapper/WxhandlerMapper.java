package com.back.mapper;

import com.back.entity.pojo.Wxhandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 微信回复消息操作表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Mapper
public interface WxhandlerMapper extends BaseMapper<Wxhandler> {

    @Select("select * from wxhandler")
    List<Wxhandler> getAll();
}
