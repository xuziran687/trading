package com.longan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.pojo.entity.UserProfile;
import com.longan.service.UserProfileService;
import com.longan.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【user_profile(用户详情表)】的数据库操作Service实现
* @createDate 2026-02-05 14:28:56
*/
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile>
    implements UserProfileService{

}




