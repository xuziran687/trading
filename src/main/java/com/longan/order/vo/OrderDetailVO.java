package com.longan.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class OrderDetailVO {
    private Long id;
    private String orderNo;
    private Long buyerId;
    private Long sellerId;

    private Long goodsId;
    private String goodsTitle;
    private String goodsImage;

    private BigDecimal price;
    private Integer status;
    private String deliveryNo;
    private LocalDateTime payTime;
    private LocalDateTime sendTime;
    private LocalDateTime finishTime;
    private LocalDateTime createTime;
    // 收货地址
    private String receiver;
    private String phone;
    private String address;
}
