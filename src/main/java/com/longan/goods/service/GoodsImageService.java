package com.longan.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.goods.entity.GoodsImage;

public interface GoodsImageService {
    void insert(GoodsImage goodsImage);

    GoodsImage getFirstImageByGoodsId(Long goodsId);
}
