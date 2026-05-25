package com.longan.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户钱包表
 *
 * @TableName user_wallet
 */
@TableName(value = "user_wallet")
@Data
public class UserWallet implements Serializable {
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
     * 平台币余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal freeze;

    /**
     * 总收入
     */
    private BigDecimal totalIncome;

    /**
     * 总支出
     */
    private BigDecimal totalOutcome;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}