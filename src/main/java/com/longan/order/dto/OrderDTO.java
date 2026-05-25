package com.longan.order.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long goodsId;
    private String receiver;
    private String phone;
    private String address;
}
