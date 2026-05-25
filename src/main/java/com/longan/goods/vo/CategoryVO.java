package com.longan.goods.vo;

import lombok.Data;

@Data
public class CategoryVO {
    Long id;
    Long parentId;
    String name;
    Integer sort;
}
