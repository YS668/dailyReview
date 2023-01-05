package com.back.service.impl;

import com.back.entity.pojo.Review;
import com.back.mapper.ReviewMapper;
import com.back.service.ReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 复盘表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

}
