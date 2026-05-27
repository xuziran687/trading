package com.longan.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.user.entity.WalletLog;
import org.apache.ibatis.annotations.Select;

public interface WalletLogMapper extends BaseMapper<WalletLog> {
    @Select("select * from user_wallet_log where user_id = #{userId}")
    WalletLog selectByUserId(Long userId);

}
