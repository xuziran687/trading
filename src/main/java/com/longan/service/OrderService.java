package com.longan.service;

import com.longan.pojo.DTO.OrderDTO;
import com.longan.pojo.VO.MyOrderVO;
import com.longan.pojo.VO.OrderVO;
import com.longan.pojo.entity.Order;
import com.longan.result.PageResult;

/**
 * @author hp
 * @description 针对表【order(订单表)】的数据库操作Service
 * @createDate 2026-02-05 13:33:03
 */
public interface OrderService {

    OrderVO create(OrderDTO orderDTO);

    PageResult<MyOrderVO> getMyOrder(Integer role, Integer status, Integer page, Integer size);

    Order getById(Long id);

    void update(Order order);
}
