package com.longan.pojo.VO;

import lombok.Data;

@Data
public class CategoryVO {
    Long id;
    Long parentId;
    String name;
    Integer sort;
}
