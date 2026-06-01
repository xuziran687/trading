package com.longan.user.service;

import com.longan.user.dto.LoginDTO;
import com.longan.user.dto.UserInfoDTO;
import com.longan.user.entity.User;
import com.longan.user.entity.UserProfile;
import com.longan.user.vo.LoginVO;

/**
* @author hp
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-02-05 13:43:12
*/
public interface UserService{
    LoginVO login(LoginDTO loginDTO);

    void insert(User user);

    User selectById(Long userId);

    void update(UserInfoDTO dto);
}
