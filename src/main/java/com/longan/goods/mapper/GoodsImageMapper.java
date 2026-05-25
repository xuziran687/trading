package com.longan.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.goods.entity.GoodsImage;

import java.util.List;

public interface GoodsImageMapper extends BaseMapper<GoodsImage> {
    void insertBatch(List<GoodsImage> imageList);
}
