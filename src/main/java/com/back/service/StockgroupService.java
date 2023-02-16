package com.back.service;

import java.util.List;

import com.back.entity.pojo.Stockgroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自选分组 服务类
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
public interface StockgroupService extends IService<Stockgroup> {

    List<String> getAllGroup(Long uid);
}
