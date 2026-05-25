package com.longan.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评价表
 * @TableName order_review
 */
@TableName(value ="order_review")
@Data
public class Review implements Serializable {
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
     * 评价人ID
     */
    private Long buyerId;

    /**
     * 被评价人ID
     */
    private Long sellerId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 1-5分
     */
    private Integer score;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}