package com.back.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.back.entity.pojo.Recomm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 建议表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-02-22
 */
@Mapper
public interface RecommMapper extends BaseMapper<Recomm> {

}
