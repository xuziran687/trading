package com.longan.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.user.entity.UserCredit;
import com.longan.user.vo.UserCreditVO;

public interface UserCreditService extends IService<UserCredit> {
    UserCreditVO getUserCredit(Long userId);
}
