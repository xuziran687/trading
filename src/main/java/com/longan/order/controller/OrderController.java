package com.longan.order.controller;

import com.longan.order.dto.OrderDTO;
import com.longan.order.dto.RefundDTO;
import com.longan.order.vo.MyOrderVO;
import com.longan.order.vo.OrderVO;
import com.longan.order.entity.Order;
import com.longan.result.PageResult;
import com.longan.result.Result;
import com.longan.order.service.OrderService;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "平台币交易")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单", description = "生成订单记录，状态为待支付")
    @PostMapping("/create")
    public Result create(@RequestBody OrderDTO orderDTO) {
        log.info("用户{}创建订单，商品ID:{}", UserContext.getUserId(), orderDTO.getGoodsId());
        OrderVO orderVO = orderService.create(orderDTO);
        return Result.success(orderVO);
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public Result orderDetail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        return Result.success(orderService.getOrderDetail(id));
    }

    @Operation(summary = "我的订单列表")
    @GetMapping("/my")
    public Result myOrder(
            @Parameter(description = "角色：1-买家，2-卖家") Integer role,
            @Parameter(description = "状态：0-待支付, 1-待发货, 2-待收货, 3-已完成, 4-已取消") Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("查询我的订单列表，角色:{}，状态:{}", role, status);
        PageResult<MyOrderVO> pageResult = orderService.getMyOrder(role, status, page, size);
        return Result.success(pageResult);
    }

    @Operation(summary = "购买商品")
    @PostMapping("/buy")
    @Transactional(rollbackFor = Exception.class)
    public Result buy(@RequestParam Long id) {
        Long buyerId = UserContext.getUserId();
        log.info("用户 {} 尝试购买订单 {}", buyerId, id);
        orderService.buy(id);
        return Result.success();
    }

    @Operation(summary = "卖家发货")
    @PostMapping("/{id}/send")
    @Transactional(rollbackFor = Exception.class)
    public Result send(@PathVariable Long id, @RequestBody String deliveryNo) {
        log.info("订单{}已发货，物流号:{}", id, deliveryNo);
        orderService.send(id, deliveryNo);
        return Result.success("已标记为发货");
    }

    @Operation(summary = "确认收货")
    @PostMapping("/{id}/confirm")
    @Transactional(rollbackFor = Exception.class)
    public Result confirm(@PathVariable Long id) {
        log.info("订单{}确认收货", id);
        orderService.confirm(id);
        return Result.success("交易完成");
    }

    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    @Transactional(rollbackFor = Exception.class)
    public Result cancel(@PathVariable Long id) {
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success("订单已取消");
    }

    @Operation(summary = "申请退款")
    @PostMapping("/refund/apply")
    @Transactional(rollbackFor = Exception.class)
    public Result applyRefund(@RequestBody RefundDTO refundDTO) {
        log.info("用户{}申请退款，订单ID:{}", UserContext.getUserId(), refundDTO.getOrderId());
        orderService.applyRefund(refundDTO);
        return Result.success("申请已提交");
    }

    @Operation(summary = "卖家同意退款")
    @PostMapping("/refund/{id}/agree")
    @Transactional(rollbackFor = Exception.class)
    public Result agreeRefund(@PathVariable Long id) {
        log.info("卖家同意退款，订单ID:{}", id);
        orderService.agreeRefund(id);
        return Result.success("已同意退款，金额已返还买家");
    }

    @Operation(summary = "卖家拒绝退款")
    @PostMapping("/refund/{id}/reject")
    @Transactional(rollbackFor = Exception.class)
    public Result rejectRefund(@PathVariable Long id) {
        log.info("卖家拒绝退款，订单ID:{}", id);
        orderService.rejectRefund(id);
        return Result.success("已拒绝退款");
    }
}
