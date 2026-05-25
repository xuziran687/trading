package com.longan.goods.service;

import com.longan.goods.vo.CategoryVO;

import java.util.List;

/**
 * @author hp
 * @description 针对表【category(商品分类表)】的数据库操作Service
 * @createDate 2026-02-05 14:35:08
 */
public interface CategoryService {

    List<CategoryVO> getLevel1();

    List<CategoryVO> getByParentId(Long parentId);
}
