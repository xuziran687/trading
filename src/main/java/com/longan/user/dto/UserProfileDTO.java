package com.longan.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {
    private Integer gender;
    private LocalDate birthday;
    private String address;
    private String signature;
}
