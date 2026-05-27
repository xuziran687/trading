package com.longan.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.longan.utils.UserContext;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @TableName order
 */
@TableName(value = "`order`")
@Data
public class Order implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 平台币支付金额
     */
    private BigDecimal pointAmount;

    /**
     * 0待付款 1待发货 2待收货 3已完成 4已取消 5退款中
     */
    private Integer status;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 物流单号
     */
    private String deliveryNo;

    /**
     * 发货时间
     */
    private LocalDateTime sendTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Boolean isBuyer(Long userId){
        if(userId != buyerId){
            return false;
        }
        return true;
    }
}