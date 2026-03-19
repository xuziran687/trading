package com.longan.service;

import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author hp
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-02-05 13:43:12
*/
public interface UserService extends IService<UserEntity> {
    UserEntity register(RegisterDTO registerDTO);
    UserEntity login(LoginDTO loginDTO);
    UserProfileEntity getProfile(Long userId);

}
