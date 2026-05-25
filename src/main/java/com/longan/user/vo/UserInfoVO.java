package com.longan.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoVO {

    private Long userId;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer status;
}
