package com.longan.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.user.dto.UserBehaviorDTO;
import com.longan.user.entity.UserBehavior;
import com.longan.user.mapper.UserBehaviorMapper;
import com.longan.user.service.UserBehaviorService;
import org.springframework.stereotype.Service;

@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {
    private final UserBehaviorMapper userBehaviorMapper;

    public UserBehaviorServiceImpl(UserBehaviorMapper userBehaviorMapper) {
        this.userBehaviorMapper = userBehaviorMapper;
    }

    @Override
    public void insert(UserBehaviorDTO userBehaviorDTO) {
        UserBehavior userBehavior = new UserBehavior();
        userBehavior.set(userBehaviorDTO);
        userBehaviorMapper.insert(userBehavior);
    }
}
