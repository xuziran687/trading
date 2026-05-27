package com.longan.goods.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GoodsDetailsVO {
    private Long id;
    private String  title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long   categoryId;
    private String   categoryName;
    private Long    userId;
    private String   nickname;
    private String avatar;
    private Integer  quality;
    private Integer   status;
    private Integer viewCount;
    private Integer collectCount;
    private List<String> imageUrls;
    private LocalDateTime updateTime;
}
