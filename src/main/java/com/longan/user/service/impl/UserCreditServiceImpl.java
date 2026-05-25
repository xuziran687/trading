package com.longan.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.user.entity.UserCredit;
import com.longan.user.mapper.UserCreditMapper;
import com.longan.user.service.UserCreditService;
import com.longan.user.vo.UserCreditVO;
import org.springframework.stereotype.Service;

@Service
public class UserCreditServiceImpl extends ServiceImpl<UserCreditMapper, UserCredit> implements UserCreditService {
    @Override
    public UserCreditVO getUserCredit(Long userId) {
        UserCredit userCredit = baseMapper.selectByUserId(userId);
        UserCreditVO userCreditVO = new UserCreditVO();
        if (userCredit != null) {
            userCreditVO.set(userCredit);
        }
        return userCreditVO;
    }
}
