package com.longan.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品分类表
 *
 * @TableName goods_category
 */
@TableName(value = "goods_category")
@Data
public class Category implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID（一级为0）
     */
    private Long parentId;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 0禁用 1启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}