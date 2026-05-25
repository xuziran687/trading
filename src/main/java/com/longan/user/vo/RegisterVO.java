package com.longan.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVO {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
}
