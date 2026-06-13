package com.longan.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.longan.order.entity.Order;
import com.longan.order.entity.Review;
import com.longan.order.service.OrderService;
import com.longan.order.service.ReviewService;
import com.longan.result.Result;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单评价")
@RestController
@RequestMapping("/api/order/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderService orderService;

    @Operation(summary = "创建评价")
    @PostMapping
    public Result create(@RequestBody Review review) {
        Long userId = UserContext.getUserId();
        Order order = orderService.getById(review.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            return Result.error("只有买家可以评价");
        }
        if (order.getStatus() != 3) {
            return Result.error("只能评价已完成的订单");
        }

        Review existing = reviewService.lambdaQuery()
                .eq(Review::getOrderId, review.getOrderId())
                .eq(Review::getBuyerId, userId)
                .one();
        if (existing != null) {
            return Result.error("已评价过该订单");
        }

        review.setBuyerId(userId);
        review.setSellerId(order.getSellerId());
        review.setGoodsId(order.getGoodsId());
        reviewService.save(review);
        return Result.success();
    }

    @Operation(summary = "获取商品评价列表")
    @GetMapping("/goods/{goodsId}")
    public Result listByGoods(@PathVariable Long goodsId,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        Page<Review> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>(Review.class)
                .eq(Review::getGoodsId, goodsId)
                .orderByDesc(Review::getCreateTime);
        IPage<Review> reviewPage = Db.page(pageInfo, wrapper);
        return Result.success(reviewPage);
    }
}
