package com.longan.goods.service;

import com.longan.goods.dto.GoodsDTO;
import com.longan.goods.dto.GoodsQueryDTO;
import com.longan.goods.dto.MyGoodsQueryDTO;
import com.longan.goods.vo.GoodsDetailsVO;
import com.longan.goods.vo.GoodsListVO;
import com.longan.goods.entity.Goods;
import com.longan.result.PageResult;

public interface GoodsService {

    PageResult<GoodsListVO> pageQuery(GoodsQueryDTO goodsQueryDTO);

    GoodsDetailsVO getGoodsDetails(Long id);

    PageResult<GoodsListVO> getMyGoods(MyGoodsQueryDTO query);

    void insert(Goods goods);

    void updateById(Goods goods);

    Goods selectById(Long id);

    void publish(GoodsDTO goodsDTO);
}
