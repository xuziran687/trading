package com.longan.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.user.entity.UserCredit;
import com.longan.user.vo.UserCreditVO;

public interface UserCreditService {
    UserCreditVO getUserCredit(Long userId);

    void init(Long id);
}
