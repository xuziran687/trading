package com.longan.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.order.entity.Review;
import com.longan.order.mapper.ReviewMapper;
import com.longan.order.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
}
