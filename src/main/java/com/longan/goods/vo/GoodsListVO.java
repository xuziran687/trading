package com.longan.goods.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GoodsListVO {
    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long userId;
    private Integer quality;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createTime;
    private List<String> imageUrls;
    private String nickname;
    private String avatar;
}
