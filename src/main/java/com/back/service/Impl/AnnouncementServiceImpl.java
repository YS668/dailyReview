package com.back.service.impl;

import com.back.entity.pojo.Announcement;
import com.back.mapper.AnnouncementMapper;
import com.back.service.AnnouncementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

}
