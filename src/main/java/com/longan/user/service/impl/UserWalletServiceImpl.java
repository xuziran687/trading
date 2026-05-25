package com.longan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.longan.user.mapper.UserWalletMapper;
import com.longan.user.entity.UserWallet;
import com.longan.user.service.UserWalletService;
import com.longan.user.vo.UserWalletVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWalletServiceImpl implements UserWalletService {
    private final UserWalletMapper walletMapper;

    @Override
    public void insert(UserWallet wallet) {
        walletMapper.insert(wallet);
    }

    @Override
    public UserWallet selectByUserId(Long id) {
        return walletMapper.selectByUserId(id);
    }

    @Override
    public void updateByUserId(UserWallet userWallet) {
        walletMapper.update(new LambdaUpdateWrapper<UserWallet>()
                .eq(UserWallet::getUserId, userWallet.getUserId())
                .set(userWallet.getBalance() != null, UserWallet::getBalance, userWallet.getBalance())
                .set(userWallet.getTotalOutcome() != null, UserWallet::getTotalOutcome, userWallet.getTotalOutcome())
        );
    }

    @Override
    public UserWalletVO getWallet(Long userId) {
        UserWallet userWallet = walletMapper.selectByUserId(userId);
        UserWalletVO userWalletVO = new UserWalletVO(userWallet.getBalance(),
                userWallet.getTotalOutcome(),
                userWallet.getFreeze(),
                userWallet.getTotalIncome());

        return userWalletVO;
    }
}
