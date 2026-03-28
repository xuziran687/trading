package com.longan.service.impl;

import com.longan.mapper.GoodsImageMapper;
import com.longan.pojo.entity.GoodsImage;
import com.longan.service.GoodsImageService;
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
}
