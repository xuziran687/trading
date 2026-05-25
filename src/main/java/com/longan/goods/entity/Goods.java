package com.longan.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表
 *
 * @TableName goods
 */
@TableName(value = "goods")
@Data
public class Goods implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 卖家ID
     */
    private Long userId;

    /**
     * 1全新 299新 395新 4九成新 5七成新及以下
     */
    private Integer quality;

    /**
     * 0下架 1上架 2已卖出
     */
    private Integer status;

    /**
     * 0未删 1已删
     */
    private Integer isDeleted;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 收藏量
     */
    private Integer collectCount;

    /**
     * 发布时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}