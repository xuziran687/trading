package com.longan.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.user.entity.UserCredit;
import org.apache.ibatis.annotations.Select;

public interface UserCreditMapper extends BaseMapper<UserCredit> {
    @Select("select * from user_credit where user_id = #{userId}")
    UserCredit selectByUserId(Long userId);
}
