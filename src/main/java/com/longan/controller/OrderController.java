package com.longan.controller;

import com.longan.pojo.DTO.OrderDTO;
import com.longan.pojo.VO.MyOrderVO;
import com.longan.pojo.VO.OrderVO;
import com.longan.pojo.entity.Order;
import com.longan.pojo.entity.UserWallet;
import com.longan.result.PageResult;
import com.longan.result.Result;
import com.longan.service.GoodsService;
import com.longan.service.OrderService;
import com.longan.service.UserWalletService;
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

    private final GoodsService goodsService;
    private final UserWalletService walletService;
    private final OrderService orderService;
    /*
            1. 状态锁定的“原子性”
        创建订单时：必须实现“下单即锁”。一旦 create 成功，goods 表的状态必须立刻变为 3 (锁定)。

        并发防范：在校验 status == 1 时，SQL 最好带上条件更新，防止两个用户同时下单同一件商品。

        UPDATE goods SET status = 3 WHERE id = ? AND status = 1 (返回影响行数为 1 才算成功)

        2. 资金流转的“暂存机制”
        支付时 (/pay)：钱从买家钱包扣除，但不要立刻打给卖家。

        逻辑建议：在订单表增加一个 pay_amount 字段记录已付金额。此时资金处于“平台托管”状态（虽然你没写中间账户，但逻辑上这笔钱已经从买家那消失了，卖家也还没拿到）。

        3. 权限校验的“严谨性”
        在拆分后的接口中，身份校验是重中之重：

        /pay 和 /confirm：必须校验 buyer_id == UserContext.getUserId()。

        /{id}/send：必须校验 seller_id == UserContext.getUserId()。

        防越权：防止 A 用户调用接口去确认 B 用户的订单。

        4. 结算触发的“终点线”
        确认收货 (/confirm)：这是唯一触发卖家加钱的时刻。

        逻辑顺序：先改订单状态 -> 后给卖家加钱 -> 最后改商品状态为 2 (已售出)。

        必须事务：这三步必须包在 @Transactional 里，任何一步失败都要回滚，否则钱就“丢”了。
     */

    // 23. 创建订单（下单但不支付，锁定库存）
    @Operation(summary = "创建订单", description = "生成订单记录，状态为待支付")
    @PostMapping("/create")
    public Result create(@RequestBody OrderDTO orderDTO) {
        // 逻辑：1.校验商品状态 2.生成唯一订单号 3.插入Order表 4.设置过期时间
        log.info("用户{}创建订单，商品ID:{}", UserContext.getUserId(), orderDTO.getGoodsId());
        // 创建订单表和订单地址表
        OrderVO orderVO = orderService.create(orderDTO);
        return Result.success(orderVO);
    }

    @Operation(summary = "购买商品")
    @PostMapping("/buy")
    @Transactional(rollbackFor = Exception.class) // 关键：开启事务，一旦报错全部回滚
    public Result buy(@RequestParam Long id) {//订单id
        Long buyerId = UserContext.getUserId();
        log.info("用户 {} 尝试购买商品 {}", buyerId, id);

        // 1. 获取订单信息（防止重复购买）
        Order order = orderService.getById(id);

        //2.更新订单
        orderService.update(order);


        // 3.冻结卖家付款的钱
        UserWallet buyerWallet = walletService.selectByUserId(order.getBuyerId());
        if (buyerWallet.getBalance().compareTo(order.getPrice()) < 0) {
            return Result.error("余额不足，请先充值");
        }
        buyerWallet.setBalance(buyerWallet.getBalance().subtract(order.getPrice()));
        buyerWallet.setFreeze(buyerWallet.getFreeze().add(order.getPrice()));
        walletService.updateByUserId(buyerWallet);

        return Result.success();
    }

    // 27. 卖家发货
    @Operation(summary = "卖家发货")
    @PostMapping("/{id}/send")
    public Result send(@PathVariable Long id, @RequestBody String deliveryNo) {
        log.info("订单{}已发货，物流号:{}", id, deliveryNo);
        // 逻辑：更新订单状态为 2-待收货
        return Result.success("已标记为发货");
    }

    // 28. 确认收货
    @Operation(summary = "确认收货")
    @PostMapping("/{id}/confirm")
    @Transactional(rollbackFor = Exception.class)
    public Result confirm(@PathVariable Long id) {
        log.info("订单{}确认收货", id);
        // 逻辑：1.更新订单状态为 3-已完成 2.（如果是担保交易）此时才给卖家转账
        return Result.success("交易完成");
    }


    // 25. 订单详情
    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public Result orderDetail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        return Result.success();
    }

    // 29. 取消订单
    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result cancel(@PathVariable Long id) {
        log.info("取消订单：{}", id);
        // 逻辑：1.校验状态是否可取消 2.恢复商品状态为 1-上架
        return Result.success("订单已取消");
    }

    // 30. 申请退款
    @Operation(summary = "申请退款")
    @PostMapping("/refund/apply")
    public Result applyRefund(@RequestBody Object refundDto) {
        log.info("用户申请退款");
        return Result.success("申请已提交");
    }


    // 24. 我的订单列表
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


}
