package com.longan.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品图片表
 *
 * @TableName goods_image
 */
@TableName(value = "goods_image")
@Data
public class GoodsImage implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}