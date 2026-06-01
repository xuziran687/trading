package com.longan.order.service;

import com.longan.order.dto.OrderDTO;
import com.longan.order.dto.RefundDTO;
import com.longan.order.vo.MyOrderVO;
import com.longan.order.vo.OrderVO;
import com.longan.order.entity.Order;
import com.longan.result.PageResult;
import com.longan.order.vo.OrderDetailVO;

public interface OrderService {

    OrderVO create(OrderDTO orderDTO);

    PageResult<MyOrderVO> getMyOrder(Integer role, Integer status, Integer page, Integer size);

    Order getById(Long id);

    void update(Order order);

    OrderDetailVO getOrderDetail(Long id);

    void confirm(Long id);

    void send(Long id, String deliveryNo);

    void buy(Long id);

    void cancel(Long id);

    void applyRefund(RefundDTO refundDTO);

    void agreeRefund(Long id);

    void rejectRefund(Long id);
}
