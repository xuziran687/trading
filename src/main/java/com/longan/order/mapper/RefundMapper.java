package com.longan.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.order.entity.Refund;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RefundMapper extends BaseMapper<Refund> {
    @Select("select * from order_refund where order_id=#{id}")
    Refund getByOrderId(@Param("id") Long id);
}
