package com.longan.goods.controller;

import com.longan.goods.dto.GoodsDTO;
import com.longan.goods.dto.GoodsQueryDTO;
import com.longan.goods.dto.MyGoodsQueryDTO;
import com.longan.goods.vo.GoodsDetailsVO;
import com.longan.goods.vo.GoodsListVO;
import com.longan.goods.entity.Goods;
import com.longan.result.PageResult;
import com.longan.result.Result;
import com.longan.goods.service.GoodsImageService;
import com.longan.goods.service.GoodsService;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
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
    private final GoodsService goodsService;
    private final GoodsImageService goodsImageService;

    @Operation(summary = "发布商品")
    @PostMapping
    public Result release(@RequestBody GoodsDTO goodsDTO) {
        log.info("发布商品：{}", goodsDTO);
        goodsService.publish(goodsDTO);
        return Result.success();
    }

    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        log.info("商品详情：{}", id);
        GoodsDetailsVO goodsDetailsVO = goodsService.getGoodsDetails(id);
        return Result.success(goodsDetailsVO);
    }

    //商品列表（搜索 / 筛选）
    //GET /api/goods/list?keyword = 手机 & categoryId=101&minPrice=1000&maxPrice=5000&sort=1&page=1&size=10
    @Operation(summary = "商品列表")
    @GetMapping("/list")
    public Result list(GoodsQueryDTO query) {
        PageResult<GoodsListVO> pageResult = goodsService.pageQuery(query);
        return Result.success(pageResult);
    }

    @Operation(summary = "我的商品")
    @GetMapping("/my")
    public Result my(MyGoodsQueryDTO query) {
        PageResult<GoodsListVO> pageResult = goodsService.getMyGoods(query);
        return Result.success(pageResult);
    }

    //18. 修改商品
    //PUT /api/goods/{id}
    @Operation(summary = "修改商品")
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody GoodsDTO goodsDTO) {
        log.info("修改商品：{}", goodsDTO);
        Goods existing = goodsService.selectById(id);
        if (existing == null) {
            return Result.error("商品不存在");
        }
        if (!existing.getUserId().equals(UserContext.getUserId())) {
            return Result.error("无权修改该商品");
        }
        Goods goods = new Goods();
        goods.setId(id);
        goods.setTitle(goodsDTO.getTitle());
        goods.setDescription(goodsDTO.getDescription());
        goods.setPrice(goodsDTO.getPrice());
        goods.setOriginalPrice(goodsDTO.getOriginalPrice());
        goods.setCategoryId(goodsDTO.getCategoryId());
        goods.setQuality(goodsDTO.getQuality());
        goodsService.updateById(goods);
        return Result.success();

    }

    // 上下架商品
    //POST /api/goods/{id}/status
    @Operation(summary = "上下架商品")
    @PostMapping("/{id}/status")
    public Result updateStatus(@PathVariable Long id, Integer status) {
        log.info("修改商品状态：{}", status);
        Goods existing = goodsService.selectById(id);
        if (existing == null) {
            return Result.error("商品不存在");
        }
        if (!existing.getUserId().equals(UserContext.getUserId())) {
            return Result.error("无权操作该商品");
        }
        Goods goods = new Goods();
        goods.setId(id);
        goods.setStatus(status);
        goodsService.updateById(goods);
        return Result.success();
    }

    // 20. 删除商品
    //DELETE /api/goods/{id}
    @Operation
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除商品：{}", id);
        Goods existing = goodsService.selectById(id);
        if (existing == null) {
            return Result.error("商品不存在");
        }
        if (!existing.getUserId().equals(UserContext.getUserId())) {
            return Result.error("无权删除该商品");
        }
        Goods goods = new Goods();
        goods.setId(id);
        goods.setIsDeleted(1);
        goodsService.updateById(goods);
        return Result.success();

    }
}
