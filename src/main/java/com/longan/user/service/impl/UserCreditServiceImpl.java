package com.longan.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.user.entity.UserCredit;
import com.longan.user.mapper.UserCreditMapper;
import com.longan.user.mapper.UserMapper;
import com.longan.user.service.UserCreditService;
import com.longan.user.vo.UserCreditVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserCreditServiceImpl implements UserCreditService {
    private final UserCreditMapper userCreditMapper;
    @Override
    public UserCreditVO getUserCredit(Long userId) {
        UserCredit userCredit = userCreditMapper.selectByUserId(userId);
        UserCreditVO userCreditVO = new UserCreditVO();
        if (userCredit != null) {
            userCreditVO.set(userCredit);
        }
        return userCreditVO;
    }

    @Override
    public void init(Long id) {
        UserCredit credit = new UserCredit();
        credit.setUserId(id);
        credit.setScore(100);
        credit.setLevel(1);
        credit.setGoodNum(0);
        credit.setBadNum(0);
        credit.setCreateTime(new Date());
        credit.setUpdateTime(new Date());
        userCreditMapper.insert(credit);
    }
}
