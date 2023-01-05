package com.back.service.impl;

import com.back.entity.pojo.Stock;
import com.back.mapper.StockMapper;
import com.back.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 股票表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

}
