package com.longan.goods.service;

import com.longan.goods.dto.GoodsDTO;
import com.longan.goods.dto.GoodsQueryDTO;
import com.longan.goods.vo.GoodsDetailsVO;
import com.longan.goods.entity.Goods;
import com.longan.result.PageResult;

/**
 * @author hp
 * @description 针对表【goods(商品表)】的数据库操作Service
 * @createDate 2026-02-05 13:29:39
 */
public interface GoodsService {

    PageResult pageQuery(GoodsQueryDTO goodsQueryDTO);

    GoodsDetailsVO getGoodsDetails(Long id);

    PageResult getMyGoods(Integer page, Integer size);


    void insert(Goods goods);

    void updateById(Goods goods);

    Goods selectById(Long id);

    void publish(GoodsDTO goodsDTO);
}
