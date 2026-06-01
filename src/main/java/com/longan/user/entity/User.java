package com.longan.user.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.longan.user.dto.UserInfoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(fill = FieldFill.INSERT)
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    @TableField(fill = FieldFill.INSERT)
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 0禁用 1正常
     */
    private Integer status;

    /**
     * 0未删 1已删
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public User(String Email, String Password) {
        this.username = Email;
        this.password = Password;
    }

    public void update(UserInfoDTO dto) {
        this.username = dto.getUsername();
        this.nickname = dto.getNickname();
        this.avatar = dto.getAvatar();
    }
}