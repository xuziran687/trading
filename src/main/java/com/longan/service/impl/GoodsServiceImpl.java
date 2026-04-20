package com.longan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.longan.mapper.CategoryMapper;
import com.longan.mapper.GoodsImageMapper;
import com.longan.mapper.GoodsMapper;
import com.longan.mapper.UserMapper;
import com.longan.pojo.DTO.GoodsDTO;
import com.longan.pojo.DTO.GoodsQueryDTO;
import com.longan.pojo.VO.GoodsDetailsVO;
import com.longan.pojo.entity.Category;
import com.longan.pojo.entity.Goods;
import com.longan.pojo.entity.GoodsImage;
import com.longan.pojo.entity.User;
import com.longan.result.PageResult;
import com.longan.service.GoodsService;
import com.longan.service.UserService;
import com.longan.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final UserMapper userMapper;

    @Override
    public PageResult pageQuery(GoodsQueryDTO query) {
        // 1. 创建分页对象 (MyBatis Plus 标准分页)
        Page<Goods> pageInfo = new Page<>(query.getPage(), query.getSize());

        // 2. 构造查询条件
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>(Goods.class);

        // 模糊匹配标题 (只有当 keyword 有值时才拼接 SQL)
        wrapper.like(StringUtils.hasText(query.getKeyword()), Goods::getTitle, query.getKeyword());

        // 精确匹配分类
        wrapper.eq(query.getCategoryId() != null, Goods::getCategoryId, query.getCategoryId());

        // 价格区间查询 (ge: 大于等于, le: 小于等于)
        wrapper.ge(query.getMinPrice() != null, Goods::getPrice, query.getMinPrice());
        wrapper.le(query.getMaxPrice() != null, Goods::getPrice, query.getMaxPrice());

        // 只展示正常上架且未删除的商品
        wrapper.eq(Goods::getStatus, 1);
        wrapper.eq(Goods::getIsDeleted, 0);

        // --- 排序逻辑 ---
        // 1-综合(默认), 2-最新, 3-价格升序, 4-价格降序 (我帮你加了个降序，更完整)
        if (query.getSort() != null) {
            switch (query.getSort()) {
                case 2: // 最新发布
                    wrapper.orderByDesc(Goods::getCreateTime);
                    break;
                case 3: // 价格从低到高
                    wrapper.orderByAsc(Goods::getPrice);
                    break;
                case 4: // 价格从高到低
                    wrapper.orderByDesc(Goods::getPrice);
                    break;
                default: // 综合排序：默认按更新时间或创建时间
                    wrapper.orderByDesc(Goods::getUpdateTime);
                    break;
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(Goods::getUpdateTime);
        }

        // 4. 执行分页查询并返回
        IPage<Goods> page = Db.page(pageInfo, wrapper);

        PageResult<Goods> pageResult = new PageResult<>();
        pageResult.setPages(page.getPages());
        pageResult.setTotal(page.getTotal());
        pageResult.setList(page.getRecords());
        return pageResult;
    }

    @Override
    @Transactional //多表操作必须加事务管理
    public GoodsDetailsVO getGoodsDetails(Long id) {
        //获得商品基本信息
        Goods goods = goodsMapper.selectById(id);
        //获得商品作者信息
        User user = userMapper.selectById(goods.getUserId());
        //获得商品图片信息
        List<GoodsImage> goodsImageList = goodsImageMapper.selectList(
                new LambdaQueryWrapper<GoodsImage>()
                        .eq(GoodsImage::getGoodsId, id)
        );
        //获得商品分类信息
        Category category = categoryMapper.selectById(goods.getCategoryId());

        //封装VO
        GoodsDetailsVO goodsDetailsVO = new GoodsDetailsVO();
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
    public PageResult getMyGoods(Integer page, Integer size) {
        Long userId = UserContext.getUserId();
        Page<Goods> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>(Goods.class);
        wrapper.eq(Goods::getUserId, userId);
        IPage<Goods> pages = Db.page(pageInfo, wrapper);
        PageResult<Goods> pageResult = new PageResult<>();
        pageResult.setPages(pages.getPages());
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
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


}




