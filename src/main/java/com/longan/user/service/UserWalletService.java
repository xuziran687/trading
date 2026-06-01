package com.longan.user.service;

import com.longan.user.entity.UserWallet;
import com.longan.user.vo.UserWalletVO;

public interface UserWalletService {

    UserWallet selectByUserId(Long id);

    void updateByUserId(UserWallet userWallet);
    UserWalletVO getWallet(Long userId);

    void init(Long id);
}
