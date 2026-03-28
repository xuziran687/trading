package com.longan.service;

import com.longan.pojo.entity.UserWallet;

public interface UserWalletService {
    void insert(UserWallet wallet);

    UserWallet selectByUserId(Long id);

    void updateByUserId(UserWallet userWallet);
}
