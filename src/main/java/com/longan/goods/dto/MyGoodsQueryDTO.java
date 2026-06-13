package com.longan.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "我的商品查询参数")
public class MyGoodsQueryDTO {

    @Schema(description = "商品状态：0-待审核，1-在售，2-下架，3-已售", required = false)
    private Integer status;

    @Schema(description = "页码", required = false)
    private Integer page = 1;

    @Schema(description = "每页数量", required = false)
    private Integer size = 12;
}
