package com.longan.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品查询过滤条件")
public class GoodsQueryDTO {

    @Schema(description = "关键词搜索（标题或描述）", required = false)
    private String keyword;

    @Schema(description = "分类ID", required = false)
    private Long categoryId;

    @Schema(description = "最低价格", required = false)
    private BigDecimal minPrice;

    @Schema(description = "最高价格", required = false)
    private BigDecimal maxPrice;

    @Schema(description = "排序方式：1-综合，2-最新，3-价格升序，4-价格降序", required = false)
    private Integer sort;

    @Schema(description = "成色：1-5级", required = false)
    private Integer quality;
    @Schema(description = "页码", required = false)
    Integer page = 1;
    @Schema(description = "每页数量", required = false)
    Integer size = 12;
}