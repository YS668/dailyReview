package com.back.service.impl;

import java.util.List;

import com.back.entity.pojo.Stockgroup;
import com.back.mapper.StockgroupMapper;
import com.back.service.StockgroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自选分组 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@Service
public class StockgroupServiceImpl extends ServiceImpl<StockgroupMapper, Stockgroup> implements StockgroupService {

    @Autowired
    private StockgroupMapper stockgroupMapper;

    @Override
    public List<String> getAllGroup(Long uid) {
        return stockgroupMapper.getAllGroup(uid);
    }
}
