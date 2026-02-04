package com.longan.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private String title;
    private String desc;
    private Double price;
    private Double originalPrice;
    private Integer categoryId;
    private Integer  quality;
    private String[] imageUrls;
}
