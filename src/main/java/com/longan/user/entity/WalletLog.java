package com.longan.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 平台币流水表
 * @TableName user_wallet_log
 */
@TableName(value ="user_wallet_log")
@Data
public class WalletLog implements Serializable {
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
     * 1收入 2支出
     */
    private Integer type;

    /**
     * 1充值 2消费 3退款 4佣金 5签到 6活动
     */
    private Integer businessType;

    /**
     * 变动金额
     */
    private BigDecimal amount;

    /**
     * 变动后余额
     */
    private BigDecimal balance;

    /**
     * 关联订单号
     */
    private String orderNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}