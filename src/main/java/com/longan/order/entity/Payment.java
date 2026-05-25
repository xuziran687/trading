package com.longan.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 支付记录表
 * @TableName order_payment
 */
@TableName(value ="order_payment")
@Data
public class Payment implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付流水号
     */
    private String payNo;

    /**
     * 1平台币 2微信 3支付宝
     */
    private Integer payType;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 0待支付 1成功 2失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}