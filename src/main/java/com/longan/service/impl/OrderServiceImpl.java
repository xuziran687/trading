package com.longan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.pojo.entity.Order;
import com.longan.service.OrderService;
import com.longan.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【order(订单表)】的数据库操作Service实现
* @createDate 2026-02-05 13:33:03
*/
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




