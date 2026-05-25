package com.longan.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.order.entity.Payment;
import com.longan.order.mapper.PaymentMapper;
import com.longan.order.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
}
