package com.longan.goods.controller;

import com.longan.goods.vo.CategoryVO;
import com.longan.result.Result;
import com.longan.goods.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类管理")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 12. 获取一级分类
     */
    @GetMapping("/level1")
    public Result<List<CategoryVO>> getLevel1() {
        // 查询 parent_id 为 0 的记录，并按 sort 排序
        List<CategoryVO> list = categoryService.getLevel1();
        return Result.success(list);
    }

    /**
     * 13. 获取子分类
     */
    @GetMapping("/children/{parentId}")
    public Result<List<CategoryVO>> getChildren(@PathVariable Long parentId) {
        // 根据传入的父 ID 查询下级，并按 sort 排序
        List<CategoryVO> list = categoryService.getByParentId(parentId);
        return Result.success(list);
    }

}
