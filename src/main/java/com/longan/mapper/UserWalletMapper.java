package com.longan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.pojo.entity.UserWallet;
import org.apache.ibatis.annotations.Select;

public interface UserWalletMapper extends BaseMapper<UserWallet> {
    @Select("select * from user_wallet where user_id = #{userId}")
    UserWallet selectByUserId(Long userId);

    void updateByUserId(UserWallet userWallet);
}
