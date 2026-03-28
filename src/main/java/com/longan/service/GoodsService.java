package com.longan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.longan.pojo.DTO.GoodsDTO;
import com.longan.pojo.DTO.GoodsQueryDTO;
import com.longan.pojo.VO.GoodsDetailsVO;
import com.longan.pojo.entity.Goods;

/**
 * @author hp
 * @description 针对表【goods(商品表)】的数据库操作Service
 * @createDate 2026-02-05 13:29:39
 */
public interface GoodsService {

    IPage<Goods> pageQuery(GoodsQueryDTO goodsQueryDTO);

    GoodsDetailsVO getGoodsDetails(Long id);

    IPage<Goods> getMyGoods(Integer page, Integer size);


    void insert(Goods goods);

    void updateById(Goods goods);

    Goods selectById(Long id);

    void publish(GoodsDTO goodsDTO);
}
