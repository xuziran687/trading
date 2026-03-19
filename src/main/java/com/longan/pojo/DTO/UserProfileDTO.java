package com.longan.pojo.DTO;

import lombok.Data;

@Data
public class UserProfileDTO {

    private String nickname;
    private String avatar;
    private String phone;
    private Integer gender;
    private String introduction;
}
