package com.longan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.pojo.DTO.UserProfileDTO;
import com.longan.pojo.entity.UserProfile;

/**
* @author hp
* @description 针对表【user_profile(用户详情表)】的数据库操作Service
* @createDate 2026-02-05 14:28:56
*/
public interface UserProfileService  {

    UserProfile getByUserId(Long userId);

    void updateByUserId(UserProfileDTO dto);

    void insert(UserProfile userProfile);
}
