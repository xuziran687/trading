package com.longan.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.order.entity.Refund;
import com.longan.order.mapper.RefundMapper;
import com.longan.order.service.RefundService;
import org.springframework.stereotype.Service;

@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {
}
