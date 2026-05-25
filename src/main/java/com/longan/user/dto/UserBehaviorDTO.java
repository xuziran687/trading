package com.longan.user.dto;


import lombok.Data;

@Data
public class UserBehaviorDTO {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 1浏览 2收藏 3加购 4下单 5搜索
     */
    private Integer behavior;

}
