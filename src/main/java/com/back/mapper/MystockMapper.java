package com.back.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.back.entity.pojo.Mystock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 自选股表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@Mapper
public interface MystockMapper extends BaseMapper<Mystock> {

}
