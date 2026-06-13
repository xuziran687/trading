package com.longan.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.longan.goods.entity.Goods;
import com.longan.goods.entity.GoodsCollect;
import com.longan.goods.service.GoodsCollectService;
import com.longan.goods.mapper.GoodsImageMapper;
import com.longan.goods.service.GoodsService;
import com.longan.goods.vo.GoodsListVO;
import com.longan.result.PageResult;
import com.longan.result.Result;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "商品收藏")
@RestController
@RequestMapping("/api/goods/collect")
@RequiredArgsConstructor
@Slf4j
public class GoodsCollectController {

    private final GoodsCollectService goodsCollectService;
    private final GoodsService goodsService;
    private final GoodsImageMapper goodsImageMapper;

    @Operation(summary = "收藏商品")
    @PostMapping("/{goodsId}")
    public Result collect(@PathVariable Long goodsId) {
        Long userId = UserContext.getUserId();
        GoodsCollect existing = goodsCollectService.lambdaQuery()
                .eq(GoodsCollect::getUserId, userId)
                .eq(GoodsCollect::getGoodsId, goodsId)
                .one();
        if (existing != null) {
            return Result.error("已收藏过该商品");
        }
        GoodsCollect collect = new GoodsCollect();
        collect.setUserId(userId);
        collect.setGoodsId(goodsId);
        goodsCollectService.save(collect);

        Goods goods = goodsService.selectById(goodsId);
        if (goods != null) {
            goods.setCollectCount(goods.getCollectCount() == null ? 1 : goods.getCollectCount() + 1);
            goodsService.updateById(goods);
        }
        return Result.success();
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping("/{goodsId}")
    public Result uncollect(@PathVariable Long goodsId) {
        Long userId = UserContext.getUserId();
        GoodsCollect existing = goodsCollectService.lambdaQuery()
                .eq(GoodsCollect::getUserId, userId)
                .eq(GoodsCollect::getGoodsId, goodsId)
                .one();
        if (existing == null) {
            return Result.error("未收藏该商品");
        }
        goodsCollectService.removeById(existing.getId());

        Goods goods = goodsService.selectById(goodsId);
        if (goods != null && goods.getCollectCount() != null && goods.getCollectCount() > 0) {
            goods.setCollectCount(goods.getCollectCount() - 1);
            goodsService.updateById(goods);
        }
        return Result.success();
    }

    @Operation(summary = "收藏列表")
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "12") Integer size) {
        Long userId = UserContext.getUserId();
        Page<GoodsCollect> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<GoodsCollect> wrapper = new LambdaQueryWrapper<>(GoodsCollect.class)
                .eq(GoodsCollect::getUserId, userId)
                .orderByDesc(GoodsCollect::getCreateTime);
        IPage<GoodsCollect> collectPage = Db.page(pageInfo, wrapper);

        List<GoodsListVO> voList = collectPage.getRecords().stream()
                .map(c -> {
                    Goods goods = goodsService.selectById(c.getGoodsId());
                    if (goods == null) return null;
                    GoodsListVO vo = new GoodsListVO();
                    vo.setId(goods.getId());
                    vo.setTitle(goods.getTitle());
                    vo.setPrice(goods.getPrice());
                    vo.setOriginalPrice(goods.getOriginalPrice());
                    vo.setUserId(goods.getUserId());
                    vo.setQuality(goods.getQuality());
                    vo.setStatus(goods.getStatus());
                    vo.setViewCount(goods.getViewCount());
                    vo.setCreateTime(goods.getCreateTime());
                    List<com.longan.goods.entity.GoodsImage> images = goodsImageMapper.selectByGoodsId(goods.getId());
                    vo.setImageUrls(images.isEmpty() ? Collections.singletonList("") : Collections.singletonList(images.get(0).getUrl()));
                    return vo;
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());

        PageResult<GoodsListVO> pageResult = new PageResult<>();
        pageResult.setPages(collectPage.getPages());
        pageResult.setTotal(collectPage.getTotal());
        pageResult.setList(voList);
        return Result.success(pageResult);
    }

    @Operation(summary = "检查是否已收藏")
    @GetMapping("/check/{goodsId}")
    public Result check(@PathVariable Long goodsId) {
        Long userId = UserContext.getUserId();
        boolean collected = goodsCollectService.lambdaQuery()
                .eq(GoodsCollect::getUserId, userId)
                .eq(GoodsCollect::getGoodsId, goodsId)
                .count() > 0;
        return Result.success(collected);
    }
}
