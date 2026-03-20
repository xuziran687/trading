package com.longan.controller;

import com.longan.pojo.DTO.GoodsDTO;
import com.longan.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
@Slf4j
public class GoodsController {
    @PostMapping
    public Result release(@RequestBody GoodsDTO goodsDTO){
        Integer goodsId=1;
        Result<Integer> result= Result.success(goodsId);
        return result;
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        Result<GoodsDTO> result= Result.success(null);
        return result;
    }

    //商品列表（搜索 / 筛选）
    //GET /api/goods/list?keyword = 手机 & categoryId=101&minPrice=1000&maxPrice=5000&sort=1&page=1&size=10
    @GetMapping("/list")
    public Result list(String keyword, Long categoryId, Double minPrice, Double maxPrice, Integer sort, Integer page, Integer size){
        return Result.success(null);
    }

    //我的商品
    //GET /api/goods/my?status=1&page=1&size=10
    @GetMapping("/my")
    public Result my(Integer status, Integer page, Integer size){
        return Result.success(null);

    }

    //18. 修改商品
    //PUT /api/goods/{id}
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody GoodsDTO goodsDTO){
        return Result.success(null);

    }

    // 上下架商品
    //POST /api/goods/{id}/status
    @PostMapping("/{id}/status")
    public Result updateStatus(@PathVariable Long id, Integer status){
        return Result.success(null);
    }
    // 20. 删除商品
    //DELETE /api/goods/{id}
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        return Result.success(null);

    }
}
