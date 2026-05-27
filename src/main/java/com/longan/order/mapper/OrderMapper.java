package com.longan.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.order.entity.Order;
import org.apache.ibatis.annotations.Update;

/**
* @author hp
* @description 针对表【order(订单表)】的数据库操作Mapper
* @createDate 2026-02-05 13:33:03
* @Entity com.longan.pojo.entity.OrderEntity
*/
public interface OrderMapper extends BaseMapper<Order> {

    @Update("update `order` set status=#{status} where id=#{id}")
    void updateStatus(Long id, Integer status);
}




