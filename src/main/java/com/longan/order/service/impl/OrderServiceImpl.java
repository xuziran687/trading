package com.longan.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.longan.order.dto.OrderDTO;
import com.longan.order.dto.RefundDTO;
import com.longan.order.entity.Order;
import com.longan.order.entity.Payment;
import com.longan.order.entity.Refund;
import com.longan.order.mapper.OrderMapper;
import com.longan.order.mapper.PaymentMapper;
import com.longan.order.mapper.RefundMapper;
import com.longan.order.service.OrderService;
import com.longan.order.vo.MyOrderVO;
import com.longan.order.vo.OrderDetailVO;
import com.longan.order.vo.OrderVO;
import com.longan.goods.entity.Goods;
import com.longan.goods.entity.GoodsImage;
import com.longan.goods.service.GoodsImageService;
import com.longan.goods.service.GoodsService;
import com.longan.result.PageResult;
import com.longan.user.entity.UserWallet;
import com.longan.user.entity.WalletLog;
import com.longan.user.mapper.UserWalletMapper;
import com.longan.user.mapper.WalletLogMapper;
import com.longan.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final GoodsService goodsService;
    private final OrderMapper orderMapper;
    private final GoodsImageService goodsImageService;
    private final UserWalletMapper walletMapper;
    private final WalletLogMapper walletLogMapper;
    private final PaymentMapper paymentMapper;
    private final RefundMapper refundMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO create(OrderDTO orderDTO) {
        Goods goods = goodsService.selectById(orderDTO.getGoodsId());
        if (goods == null || goods.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }
        if (goods.getUserId().equals(UserContext.getUserId())) {
            throw new RuntimeException("不能购买自己的商品");
        }

        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000)));
        order.setBuyerId(UserContext.getUserId());
        order.setSellerId(goods.getUserId());
        order.setGoodsId(goods.getId());
        order.setPrice(goods.getPrice());
        order.setPointAmount(goods.getPrice());
        order.setStatus(0);
        order.setReceiver(orderDTO.getReceiver());
        order.setPhone(orderDTO.getPhone());
        order.setAddress(orderDTO.getAddress());
        orderMapper.insert(order);

        goods.setStatus(3);
        goodsService.updateById(goods);

        OrderVO orderVO = new OrderVO();
        orderVO.setId(order.getId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPrice(order.getPrice());
        orderVO.setPointAmount(order.getPointAmount());
        return orderVO;
    }

    @Override
    public PageResult<MyOrderVO> getMyOrder(Integer role, Integer status, Integer page, Integer size) {
        IPage<Order> orderPage = new Page<>(page, size);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();

        if (role != null && role == 1) {
            queryWrapper.eq(Order::getBuyerId, UserContext.getUserId());
        } else {
            queryWrapper.eq(Order::getSellerId, UserContext.getUserId());
        }

        queryWrapper.eq(status != null, Order::getStatus, status);
        queryWrapper.orderByDesc(Order::getCreateTime);
        orderMapper.selectPage(orderPage, queryWrapper);

        List<MyOrderVO> voList = orderPage.getRecords().stream().map(order -> {
            MyOrderVO vo = new MyOrderVO();
            vo.setOrderId(order.getId());
            vo.setOrderNo(order.getOrderNo());
            vo.setGoodsId(order.getGoodsId());
            vo.setPrice(order.getPrice());
            vo.setStatus(order.getStatus());
            Goods goods = goodsService.selectById(order.getGoodsId());
            if (goods != null) {
                vo.setTitle(goods.getTitle());
            }
            GoodsImage image = goodsImageService.getFirstImageByGoodsId(order.getGoodsId());
            if (image != null) {
                vo.setImageUrl(image.getUrl());
            }
            vo.setCreateTime(order.getCreateTime());
            return vo;
        }).toList();

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

    @Override
    public OrderDetailVO getOrderDetail(Long id) {
        return buildOrderDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buy(Long id) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getBuyerId().equals(order.getSellerId())) {
            throw new RuntimeException("不能购买自己的商品");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }

        UserWallet buyerWallet = walletMapper.selectByUserId(order.getBuyerId());
        if (buyerWallet == null || buyerWallet.getBalance().compareTo(order.getPrice()) < 0) {
            throw new RuntimeException("余额不足，请先充值");
        }

        buyerWallet.setBalance(buyerWallet.getBalance().subtract(order.getPrice()));
        buyerWallet.setFreeze(buyerWallet.getFreeze().add(order.getPrice()));
        buyerWallet.setTotalOutcome(buyerWallet.getTotalOutcome().add(order.getPrice()));
        walletMapper.updateById(buyerWallet);

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setPayNo("PAY" + System.currentTimeMillis());
        payment.setPayType(1);
        payment.setAmount(order.getPrice());
        payment.setStatus(1);
        paymentMapper.insert(payment);

        WalletLog buyerLog = new WalletLog();
        buyerLog.setUserId(order.getBuyerId());
        buyerLog.setType(2);
        buyerLog.setBusinessType(2);
        buyerLog.setAmount(order.getPrice());
        buyerLog.setBalance(buyerWallet.getBalance());
        buyerLog.setOrderNo(order.getOrderNo());
        buyerLog.setRemark("购买商品，订单号:" + order.getOrderNo());
        walletLogMapper.insert(buyerLog);

        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确，无法确认收货");
        }

        order.setStatus(3);
        order.setFinishTime(LocalDateTime.now());
        orderMapper.updateById(order);

        UserWallet sellerWallet = walletMapper.selectByUserId(order.getSellerId());
        sellerWallet.setFreeze(sellerWallet.getFreeze().subtract(order.getPrice()));
        sellerWallet.setBalance(sellerWallet.getBalance().add(order.getPrice()));
        sellerWallet.setTotalIncome(sellerWallet.getTotalIncome().add(order.getPrice()));
        walletMapper.updateById(sellerWallet);

        WalletLog sellerLog = new WalletLog();
        sellerLog.setUserId(order.getSellerId());
        sellerLog.setType(1);
        sellerLog.setBusinessType(2);
        sellerLog.setAmount(order.getPrice());
        sellerLog.setBalance(sellerWallet.getBalance());
        sellerLog.setOrderNo(order.getOrderNo());
        sellerLog.setRemark("商品售出，订单号:" + order.getOrderNo());
        walletLogMapper.insert(sellerLog);

        Goods goods = goodsService.selectById(order.getGoodsId());
        goods.setStatus(2);
        goodsService.updateById(goods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(Long id, String deliveryNo) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getStatus() != 1) {
            throw new RuntimeException("订单状态不正确，无法发货");
        }

        order.setDeliveryNo(deliveryNo);
        order.setStatus(2);
        order.setSendTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法取消");
        }

        orderMapper.updateStatus(id, 4);

        Goods goods = goodsService.selectById(order.getGoodsId());
        goods.setStatus(1);
        goodsService.updateById(goods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRefund(RefundDTO refundDTO) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(refundDTO.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确，无法申请退款");
        }

        QueryWrapper<Refund> checkQuery = new QueryWrapper<>();
        checkQuery.eq("order_id", order.getId()).eq("status", 0);
        if (refundMapper.selectCount(checkQuery) > 0) {
            throw new RuntimeException("已有退款申请在处理中");
        }

        orderMapper.updateStatus(order.getId(), 5);

        Refund refund = new Refund();
        refund.setOrderId(order.getId());
        refund.setReason(refundDTO.getReason());
        refund.setAmount(order.getPrice());
        refund.setStatus(0);
        refundMapper.insert(refund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agreeRefund(Long id) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getStatus() != 5) {
            throw new RuntimeException("订单状态不正确，无法处理退款");
        }

        QueryWrapper<Refund> refundQuery = new QueryWrapper<>();
        refundQuery.eq("order_id", id).eq("status", 0);
        Refund refund = refundMapper.selectOne(refundQuery);
        if (refund == null) {
            throw new RuntimeException("退款申请不存在或已处理");
        }

        refund.setStatus(3);
        refund.setHandleTime(new Date());
        refundMapper.updateById(refund);

        UserWallet buyerWallet = walletMapper.selectByUserId(order.getBuyerId());
        buyerWallet.setFreeze(buyerWallet.getFreeze().subtract(order.getPrice()));
        buyerWallet.setBalance(buyerWallet.getBalance().add(order.getPrice()));
        walletMapper.updateById(buyerWallet);

        WalletLog buyerLog = new WalletLog();
        buyerLog.setUserId(order.getBuyerId());
        buyerLog.setType(1);
        buyerLog.setBusinessType(3);
        buyerLog.setAmount(order.getPrice());
        buyerLog.setBalance(buyerWallet.getBalance());
        buyerLog.setOrderNo(order.getOrderNo());
        buyerLog.setRemark("退款，订单号:" + order.getOrderNo());
        walletLogMapper.insert(buyerLog);

        order.setStatus(4);
        orderMapper.updateById(order);

        Goods goods = goodsService.selectById(order.getGoodsId());
        goods.setStatus(1);
        goodsService.updateById(goods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectRefund(Long id) {
        Long userId = UserContext.getUserId();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (order.getStatus() != 5) {
            throw new RuntimeException("订单状态不正确，无法处理退款");
        }

        QueryWrapper<Refund> refundQuery = new QueryWrapper<>();
        refundQuery.eq("order_id", id).eq("status", 0);
        Refund refund = refundMapper.selectOne(refundQuery);
        if (refund == null) {
            throw new RuntimeException("退款申请不存在或已处理");
        }

        refund.setStatus(2);
        refund.setHandleTime(new Date());
        refundMapper.updateById(refund);

        order.setStatus(2);
        orderMapper.updateById(order);
    }

    private OrderDetailVO buildOrderDetail(Long id) {
        OrderDetailVO vo = new OrderDetailVO();
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        Long goodsId = order.getGoodsId();
        Goods goods = goodsService.selectById(goodsId);
        GoodsImage image = goodsImageService.getFirstImageByGoodsId(goodsId);

        vo.setId(id);
        vo.setOrderNo(order.getOrderNo());
        vo.setBuyerId(order.getBuyerId());
        vo.setSellerId(order.getSellerId());
        vo.setGoodsId(order.getGoodsId());
        vo.setGoodsTitle(goods != null ? goods.getTitle() : null);
        vo.setGoodsImage(image != null ? image.getUrl() : null);
        vo.setPrice(order.getPrice());
        vo.setStatus(order.getStatus());
        vo.setDeliveryNo(order.getDeliveryNo());
        vo.setPayTime(order.getPayTime());
        vo.setSendTime(order.getSendTime());
        vo.setFinishTime(order.getFinishTime());
        vo.setCreateTime(order.getCreateTime());
        vo.setReceiver(order.getReceiver());
        vo.setPhone(order.getPhone());
        vo.setAddress(order.getAddress());
        return vo;
    }
}
