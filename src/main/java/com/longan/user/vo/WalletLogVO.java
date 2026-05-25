package com.longan.user.vo;

import com.longan.user.entity.WalletLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class WalletLogVO {
    Long id;
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

    public void set(WalletLog walletLog){
        this.id = walletLog.getId();
        this.type = walletLog.getType();
        this.businessType = walletLog.getBusinessType();
        this.amount = walletLog.getAmount();
        this.balance = walletLog.getBalance();
        this.orderNo = walletLog.getOrderNo();
        this.remark = walletLog.getRemark();
        this.createTime = walletLog.getCreateTime();
    }
}
