package com.longan.goods.service.impl;

import com.longan.goods.mapper.CategoryMapper;
import com.longan.goods.vo.CategoryVO;
import com.longan.goods.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hp
 * @description 针对表【category(商品分类表)】的数据库操作Service实现
 * @createDate 2026-02-05 14:35:08
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getLevel1() {
        // 获取所有一级分类
        List<CategoryVO> list = categoryMapper.getLevel1();
        return list;
    }

    @Override
    public List<CategoryVO> getByParentId(Long parentId) {
        List<CategoryVO> list = categoryMapper.getByParentId(parentId);
        return list;
    }


}




