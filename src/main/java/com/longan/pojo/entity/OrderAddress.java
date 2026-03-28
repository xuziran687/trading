package com.longan.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单地址表
 *
 * @TableName order_address
 */
@TableName(value = "order_address")
@Data
public class OrderAddress implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}