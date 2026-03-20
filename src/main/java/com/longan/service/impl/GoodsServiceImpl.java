package com.longan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.pojo.entity.Goods;
import com.longan.service.GoodsService;
import com.longan.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【goods(商品表)】的数据库操作Service实现
* @createDate 2026-02-05 13:29:39
*/
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




