package com.back.service.impl;

import com.back.entity.pojo.Mystock;
import com.back.mapper.MystockMapper;
import com.back.service.MystockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自选股表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-07
 */
@Service("mystockService")
public class MystockServiceImpl extends ServiceImpl<MystockMapper, Mystock> implements MystockService {

}
