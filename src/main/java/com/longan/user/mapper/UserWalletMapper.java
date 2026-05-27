package com.longan.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.user.entity.UserWallet;
import org.apache.ibatis.annotations.Select;

public interface UserWalletMapper extends BaseMapper<UserWallet> {
    @Select("select * from user_wallet where user_id = #{userId}")
    UserWallet selectByUserId(Long userId);

}
