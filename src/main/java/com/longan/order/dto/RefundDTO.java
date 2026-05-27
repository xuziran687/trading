package com.longan.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundDTO {
    private Long orderId;
    private String reason;
    private BigDecimal amount;
}
