package com.longan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.pojo.entity.Category;
import com.longan.service.CategoryService;
import com.longan.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【category(商品分类表)】的数据库操作Service实现
* @createDate 2026-02-05 14:35:08
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




