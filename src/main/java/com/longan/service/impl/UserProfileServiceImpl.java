package com.longan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.pojo.DTO.UserProfileDTO;
import com.longan.pojo.entity.UserCredit;
import com.longan.pojo.entity.UserProfile;
import com.longan.service.UserProfileService;
import com.longan.mapper.UserProfileMapper;
import com.longan.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【user_profile(用户详情表)】的数据库操作Service实现
* @createDate 2026-02-05 14:28:56
*/
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl  implements UserProfileService{
    private final UserProfileMapper userProfileMapperMapper;

    @Override
    public UserProfile getByUserId(Long userId) {
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userProfileMapperMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateByUserId(UserProfileDTO dto) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(UserContext.getUserId());
        userProfile.setGender(dto.getGender());
        userProfile.setBirthday(dto.getBirthday());
        userProfile.setAddress(dto.getAddress());
        userProfile.setSignature(dto.getSignature());
        userProfileMapperMapper.updateByUserId(userProfile);
    }

    @Override
    public void insert(UserProfile userProfile) {
        userProfileMapperMapper.insert(userProfile);
    }
}




