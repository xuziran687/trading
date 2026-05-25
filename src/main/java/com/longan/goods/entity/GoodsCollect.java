package com.longan.goods.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品收藏表
 * @TableName goods_collect
 */
@TableName(value ="goods_collect")
@Data
public class GoodsCollect implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 收藏时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}