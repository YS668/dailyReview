package com.back.mapper;

import com.back.entity.pojo.Announcement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 公告表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

}
