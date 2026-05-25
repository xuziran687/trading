package com.longan.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MyOrderVO {
    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "商品ID")
    private Long goodsId;

    @Schema(description = "商品标题")
    private String title;

    @Schema(description = "商品图片URL")
    private String imageUrl;

    @Schema(description = "订单价格")
    private BigDecimal price;

    @Schema(description = "状态：0待付款 1待发货 2待收货 3已完成 4已取消")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
