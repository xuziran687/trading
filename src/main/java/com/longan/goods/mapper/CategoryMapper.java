package com.longan.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.goods.vo.CategoryVO;
import com.longan.goods.entity.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hp
 * @description 针对表【category(商品分类表)】的数据库操作Mapper
 * @createDate 2026-02-05 14:35:08
 * @Entity com.longan.pojo.entity.Category
 */
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select id, parent_id, name, sort from goods_category where parent_id = 0 order by sort ")
    List<CategoryVO> getLevel1();

    @Select("select id, parent_id, name, sort from goods_category where parent_id = #{parentId} order by sort ")
    List<CategoryVO> getByParentId(Long parentId);
}




