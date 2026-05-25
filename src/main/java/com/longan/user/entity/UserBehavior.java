package com.longan.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.longan.user.dto.UserBehaviorDTO;
import com.longan.utils.UserContext;
import lombok.Data;

/**
 * 用户行为表
 * @TableName user_behavior
 */
@TableName(value ="user_behavior")
@Data
public class UserBehavior implements Serializable {
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
     * 分类ID
     */
    private Long categoryId;

    /**
     * 1浏览 2收藏 3加购 4下单 5搜索
     */
    private Integer behavior;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void set(UserBehaviorDTO userBehaviorDTO) {
        userId= UserContext.getUserId();
        goodsId = userBehaviorDTO.getGoodsId();
        categoryId = userBehaviorDTO.getCategoryId();
        behavior = userBehaviorDTO.getBehavior();
        createTime = new Date();
    }
}