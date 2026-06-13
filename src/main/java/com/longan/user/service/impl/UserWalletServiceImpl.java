package com.longan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.longan.user.mapper.UserWalletMapper;
import com.longan.user.entity.UserWallet;
import com.longan.user.service.UserWalletService;
import com.longan.user.vo.UserWalletVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserWalletServiceImpl implements UserWalletService {
    private final UserWalletMapper walletMapper;


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
        if (userWallet == null) {
            init(userId);
            userWallet = walletMapper.selectByUserId(userId);
        }
        UserWalletVO userWalletVO = new UserWalletVO(userWallet.getBalance(),
                userWallet.getTotalOutcome(),
                userWallet.getFreeze(),
                userWallet.getTotalIncome());

        return userWalletVO;
    }

    @Override
    public void init(Long id) {
        UserWallet wallet = new UserWallet();
        wallet.setUserId(id);
        wallet.setBalance(BigDecimal.ZERO);      // 余额 0
        wallet.setFreeze(BigDecimal.ZERO);       // 冻结 0
        wallet.setTotalIncome(BigDecimal.ZERO);  // 累计收入 0
        wallet.setTotalOutcome(BigDecimal.ZERO); // 累计支出 0
        walletMapper.insert(wallet);
    }
}
