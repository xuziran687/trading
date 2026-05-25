package com.longan.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private String title;//标题
    private String description;//描述
    private BigDecimal price;// 价格
    private BigDecimal originalPrice;//原价
    private Long categoryId;//分类id
    private Integer  quality;//质量
    private String[] imageUrls;// 图片
}
