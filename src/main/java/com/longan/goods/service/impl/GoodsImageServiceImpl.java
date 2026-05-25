package com.longan.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.longan.goods.mapper.GoodsImageMapper;
import com.longan.goods.entity.GoodsImage;
import com.longan.goods.service.GoodsImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsImageServiceImpl implements GoodsImageService {
    private final GoodsImageMapper goodsImageMapper;

    @Override
    public void insert(GoodsImage goodsImage) {
        goodsImageMapper.insert(goodsImage);
    }

    @Override
    public GoodsImage getFirstImageByGoodsId(Long goodsId) {
        return goodsImageMapper.selectOne(
                new LambdaQueryWrapper<GoodsImage>()
                        .eq(GoodsImage::getGoodsId, goodsId)
                        .eq(GoodsImage::getSort, 1)
        );
    }
}
