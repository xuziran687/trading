package com.longan.service;

import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.pojo.entity.User;
import com.longan.pojo.entity.UserProfile;

/**
* @author hp
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-02-05 13:43:12
*/
public interface UserService extends IService<User> {
    User register(RegisterDTO registerDTO);
    User login(LoginDTO loginDTO) throws Exception;
    UserProfile getProfile(Long userId);

}
