package com.longan.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private BigDecimal price;
    private BigDecimal pointAmount;
}
