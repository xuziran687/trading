package com.longan.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private Long userId;
    private String nickname;
    private String avatar;

    private String token;
}
