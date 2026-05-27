package com.longan.goods.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.goods.entity.GoodsImage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsImageMapper extends BaseMapper<GoodsImage> {
    void insertBatch(List<GoodsImage> imageList);
    @Select("select * from goods_image where goods_id=#{goodsId}")
    List<GoodsImage> selectByGoodsId(Long goodsId);
}
