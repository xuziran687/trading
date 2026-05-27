package com.longan.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户收货地址表
 */
@TableName(value = "`user_address`")
@Data
public class UserAddress implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String receiver;

    private String phone;

    private String address;

    private Integer isDefault;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
