package com.longan.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户钱包表
 *
 * @TableName user_wallet
 */
@Data
@AllArgsConstructor
public class UserWalletVO implements Serializable {
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}