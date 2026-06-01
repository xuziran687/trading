package com.longan.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.longan.goods.mapper.CategoryMapper;
import com.longan.goods.mapper.GoodsImageMapper;
import com.longan.goods.mapper.GoodsMapper;
import com.longan.goods.dto.GoodsDTO;
import com.longan.goods.dto.GoodsQueryDTO;
import com.longan.goods.dto.MyGoodsQueryDTO;
import com.longan.goods.vo.GoodsDetailsVO;
import com.longan.goods.vo.GoodsListVO;
import com.longan.goods.entity.Category;
import com.longan.goods.entity.Goods;
import com.longan.goods.entity.GoodsImage;
import com.longan.user.entity.User;
import com.longan.result.PageResult;
import com.longan.goods.service.GoodsService;
import com.longan.user.service.UserService;
import com.longan.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hp
 * @description 针对表【goods(商品表)】的数据库操作Service实现
 * @createDate 2026-02-05 13:29:39
 */
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final GoodsMapper goodsMapper;
    private final UserService userService;
    private final GoodsImageMapper goodsImageMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public PageResult<GoodsListVO> pageQuery(GoodsQueryDTO query) {
        Page<Goods> pageInfo = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Goods> wrapper = buildQueryWrapper(query);
        IPage<Goods> page = Db.page(pageInfo, wrapper);

        List<GoodsListVO> voList = page.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        PageResult<GoodsListVO> pageResult = new PageResult<>();
        pageResult.setPages(page.getPages());
        pageResult.setTotal(page.getTotal());
        pageResult.setList(voList);
        return pageResult;
    }



    @Override
    @Transactional //多表操作必须加事务管理
    public GoodsDetailsVO getGoodsDetails(Long id) {
        //获得商品基本信息
        Goods goods = goodsMapper.selectById(id);
        //获得商品作者信息
        User user = userService.selectById(goods.getUserId());
        //获得商品图片信息
        List<GoodsImage> goodsImageList = goodsImageMapper.selectList(
                new LambdaQueryWrapper<GoodsImage>()
                        .eq(GoodsImage::getGoodsId, id)
        );
        //获得商品分类信息
        Category category = categoryMapper.selectById(goods.getCategoryId());

        //封装VO
        GoodsDetailsVO goodsDetailsVO = new GoodsDetailsVO();
        goodsDetailsVO.setId(goods.getId());
        // 标题
        goodsDetailsVO.setTitle(goods.getTitle());
        // 描述
        goodsDetailsVO.setDescription(goods.getDescription());
        // 价格
        goodsDetailsVO.setPrice(goods.getPrice());
        // 原始价格
        goodsDetailsVO.setOriginalPrice(goods.getOriginalPrice());
        // 分类id
        goodsDetailsVO.setCategoryId(goods.getCategoryId());
        // 分类名
        goodsDetailsVO.setCategoryName(category.getName());
        goodsDetailsVO.setUserId(goods.getUserId());
        goodsDetailsVO.setNickname(user.getNickname());
        goodsDetailsVO.setAvatar(user.getAvatar());
        goodsDetailsVO.setQuality(goods.getQuality());
        goodsDetailsVO.setStatus(goods.getStatus());
        goodsDetailsVO.setViewCount(goods.getViewCount());
        goodsDetailsVO.setCollectCount(goods.getCollectCount());
        goodsDetailsVO.setUpdateTime(goods.getUpdateTime());

        List<String> imageUrls = new ArrayList<>();
        for (GoodsImage goodsImage : goodsImageList) {
            imageUrls.add(goodsImage.getUrl());
        }
        goodsDetailsVO.setImageUrls(imageUrls);
        return goodsDetailsVO;
    }

    @Override
    public PageResult<GoodsListVO> getMyGoods(MyGoodsQueryDTO query) {
        Long userId = UserContext.getUserId();
        Page<Goods> pageInfo = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>(Goods.class);
        wrapper.eq(Goods::getUserId, userId);
        wrapper.eq(query.getStatus() != null, Goods::getStatus, query.getStatus());
        IPage<Goods> pages = Db.page(pageInfo, wrapper);

        List<GoodsListVO> voList = pages.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        PageResult<GoodsListVO> pageResult = new PageResult<>();
        pageResult.setPages(pages.getPages());
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(voList);
        return pageResult;
    }

    @Override
    public void insert(Goods goods) {
        goodsMapper.insert(goods);
    }

    @Override
    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);

    }

    @Override
    public Goods selectById(Long id) {
        return goodsMapper.selectById(id);
    }

    @Override
    @Transactional // 🔴 关键：开启事务，保证商品和图片要么全成功，要么全失败
    public void publish(GoodsDTO goodsDTO) {
        // 1. 属性拷贝 (可以使用 BeanUtils 简化，或者手动 set)
        Goods goods = new Goods();
        goods.setTitle(goodsDTO.getTitle());
        goods.setDescription(goodsDTO.getDescription());
        goods.setPrice(goodsDTO.getPrice());
        goods.setOriginalPrice(goodsDTO.getOriginalPrice());
        goods.setCategoryId(goodsDTO.getCategoryId());
        goods.setQuality(goodsDTO.getQuality());

        // 2. 补充必要字段
        goods.setUserId(UserContext.getUserId()); // 获取当前登录用户ID
        goods.setStatus(1); // 默认上架状态

        // 3. 插入商品主表
        // MyBatis-Plus 插入后会自动回填 goods.id
        goodsMapper.insert(goods);

        // 4. 批量插入图片 (避免在循环里调 SQL，提升性能)
        if (goodsDTO.getImageUrls() != null && goodsDTO.getImageUrls().length > 0) {
            List<GoodsImage> imageList = Arrays.stream(goodsDTO.getImageUrls())
                    .map(url -> {
                        GoodsImage img = new GoodsImage();
                        img.setGoodsId(goods.getId()); // 使用回填的 ID
                        img.setUrl(url);
                        return img;
                    })
                    .collect(Collectors.toList());

            goodsImageMapper.insertBatch(imageList);
        }
    }
    private GoodsListVO convertToListVO(Goods goods) {
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

        List<GoodsImage> images = goodsImageMapper.selectByGoodsId(goods.getId());
        if (images != null) {
            vo.setImageUrls(Collections.singletonList(images.get(0).getUrl()));
        }
        User user = userService.selectById(goods.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }
        return vo;
    }

    private LambdaQueryWrapper<Goods> buildQueryWrapper(GoodsQueryDTO query) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>(Goods.class);

        wrapper.like(StringUtils.hasText(query.getKeyword()), Goods::getTitle, query.getKeyword());
        wrapper.eq(query.getCategoryId() != null, Goods::getCategoryId, query.getCategoryId());
        wrapper.ge(query.getMinPrice() != null, Goods::getPrice, query.getMinPrice());
        wrapper.le(query.getMaxPrice() != null, Goods::getPrice, query.getMaxPrice());
        wrapper.eq(Goods::getStatus, 1);
        wrapper.eq(Goods::getIsDeleted, 0);

        if (query.getSort() != null) {
            switch (query.getSort()) {
                case 2:
                    wrapper.orderByDesc(Goods::getCreateTime);
                    break;
                case 3:
                    wrapper.orderByAsc(Goods::getPrice);
                    break;
                case 4:
                    wrapper.orderByDesc(Goods::getPrice);
                    break;
                default:
                    wrapper.orderByDesc(Goods::getUpdateTime);
                    break;
            }
        } else {
            wrapper.orderByDesc(Goods::getUpdateTime);
        }

        return wrapper;
    }


}




