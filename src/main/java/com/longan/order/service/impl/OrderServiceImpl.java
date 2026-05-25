package com.longan.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.longan.order.mapper.OrderAddressMapper;
import com.longan.order.mapper.OrderMapper;
import com.longan.order.dto.OrderDTO;
import com.longan.order.vo.MyOrderVO;
import com.longan.order.vo.OrderVO;
import com.longan.goods.entity.Goods;
import com.longan.goods.entity.GoodsImage;
import com.longan.order.entity.Order;
import com.longan.order.entity.OrderAddress;
import com.longan.result.PageResult;
import com.longan.order.service.OrderService;
import com.longan.goods.service.GoodsService;
import com.longan.goods.service.GoodsImageService;
import com.longan.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hp
 * @description 针对表【order(订单表)】的数据库操作Service实现
 * @createDate 2026-02-05 13:33:03
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final GoodsService goodsService;
    private final OrderMapper orderMapper;
    private final OrderAddressMapper orderAddressMapper;
    private final GoodsImageService goodsImageService;

    @Override
    public OrderVO create(OrderDTO orderDTO) {
        // 逻辑：1.校验商品状态 2.生成唯一订单号 3.插入Order表
        Goods goods = goodsService.selectById(orderDTO.getGoodsId());
        if (goods == null || goods.getStatus() != 1) {
            return null;
        }
        Order order = new Order();
        // 生成唯一订单号
        order.setOrderNo(String.valueOf(goods.getId()));
        // 买家 ID
        order.setBuyerId(UserContext.getUserId());
        //卖家 ID
        order.setSellerId(goods.getUserId());
        // 商品 ID
        order.setGoodsId(goods.getId());
        // 成交价格
        order.setPrice(goods.getPrice());
        // 平台币支付金额
        order.setPointAmount(goods.getPrice());
        // 创建order表
        orderMapper.insert(order);

        OrderAddress orderAddress = new OrderAddress();
        // 订单 ID
        orderAddress.setOrderId(order.getId());
        // 收货人
        orderAddress.setReceiver(orderDTO.getReceiver());
        // 手机号
        orderAddress.setPhone(orderDTO.getPhone());
        // 收货地址
        orderAddress.setAddress(orderDTO.getAddress());
        // 创建order_address表
        orderAddressMapper.insert(orderAddress);


        //VO
        OrderVO orderVO = new OrderVO();
        orderVO.setId(order.getId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPrice(order.getPrice());
        orderVO.setPointAmount(order.getPointAmount());

        return orderVO;
    }

    @Override
    public PageResult<MyOrderVO> getMyOrder(Integer role, Integer status, Integer page, Integer size) {
        // 1. 定义 MyBatis Plus 的分页对象 IPage
        IPage<Order> orderPage = new Page<>(page, size);

        // 2. 构造查询条件
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();

        // 角色过滤：1-买家，2-卖家
        if (role != null && role == 1) {
            queryWrapper.eq(Order::getBuyerId, UserContext.getUserId());
        } else {
            queryWrapper.eq(Order::getSellerId, UserContext.getUserId());
        }

        // 状态过滤
        queryWrapper.eq(status != null, Order::getStatus, status);

        // 排序：最新订单在前
        queryWrapper.orderByDesc(Order::getCreateTime);

        // 3. 执行查询（MyBatis Plus 会自动把 count 和 limit 注入 orderPage）
        orderMapper.selectPage(orderPage, queryWrapper);

        // 4. 处理数据转换：将 IPage<Order> 转换为 List<MyOrderVO>
        List<MyOrderVO> voList = orderPage.getRecords().stream().map(order -> {
            MyOrderVO vo = new MyOrderVO();
            // 订单 ID
            vo.setOrderId(order.getId());
            // 订单编号
            vo.setOrderNo(order.getOrderNo());
            // 商品 ID
            vo.setGoodsId(order.getGoodsId());
            // 价格
            vo.setPrice(order.getPrice());
            // 状态
            vo.setStatus(order.getStatus());
            // --- 核心：去 Goods 表查标题和图片 ---
            Goods goods = goodsService.selectById(order.getGoodsId());
            if (goods != null) {
                // 标题
                vo.setTitle(goods.getTitle());
            }
            GoodsImage image = goodsImageService.getFirstImageByGoodsId(order.getGoodsId());
            if (image != null) {
                // 图片
                vo.setImageUrl(image.getUrl());
            }
            return vo;
        }).toList();

        // 5. 封装为你自定义的 PageResult
        PageResult<MyOrderVO> result = new PageResult<>();
        result.setList(voList);
        result.setTotal(orderPage.getTotal());
        result.setPages(orderPage.getPages());

        return result;
    }

    @Override
    public Order getById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void update(Order order) {
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }


}




