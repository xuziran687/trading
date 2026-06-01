package com.longan.user.service;

import com.longan.user.dto.UserProfileDTO;
import com.longan.user.entity.UserProfile;

/**
* @author hp
* @description 针对表【user_profile(用户详情表)】的数据库操作Service
* @createDate 2026-02-05 14:28:56
*/
public interface UserProfileService  {

    UserProfile getByUserId(Long userId);

    void update(UserProfileDTO dto);

    void init(Long id);
}
