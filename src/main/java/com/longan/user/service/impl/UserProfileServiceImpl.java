package com.longan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.user.mapper.UserProfileMapper;
import com.longan.user.dto.UserProfileDTO;
import com.longan.user.entity.UserProfile;
import com.longan.user.service.UserProfileService;
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
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfile getByUserId(Long userId) {
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userProfileMapper.selectOne(queryWrapper);
    }

    @Override
    public void update(UserProfileDTO dto) {
        Long userId = UserContext.getUserId();

        // 使用 UpdateWrapper 构造 SQL
        LambdaUpdateWrapper<UserProfile> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserProfile::getUserId, userId)
                .set(dto.getGender() != null, UserProfile::getGender, dto.getGender())
                .set(dto.getBirthday() != null, UserProfile::getBirthday, dto.getBirthday())
                .set(dto.getAddress() != null, UserProfile::getAddress, dto.getAddress())
                .set(dto.getSignature() != null, UserProfile::getSignature, dto.getSignature());

        userProfileMapper.update(null, updateWrapper);
    }


    @Override
    public void init(Long id) {
        UserProfile profile = new UserProfile();
        profile.setUserId(id);
        profile.setGender(0);
        userProfileMapper.insert(profile);

    }
}




