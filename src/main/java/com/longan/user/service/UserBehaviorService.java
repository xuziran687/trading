package com.longan.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.user.dto.UserBehaviorDTO;
import com.longan.user.entity.UserBehavior;

public interface UserBehaviorService extends IService<UserBehavior> {
    void insert(UserBehaviorDTO userBehaviorDTO);
}
