package com.longan.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户信用表
 * @TableName user_credit
 */
@TableName(value ="user_credit")
@Data
public class UserCredit implements Serializable {
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
     * 信用分
     */
    private Integer score;

    /**
     * 信用等级
     */
    private Integer level;

    /**
     * 好评数
     */
    private Integer goodNum;

    /**
     * 差评数
     */
    private Integer badNum;

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