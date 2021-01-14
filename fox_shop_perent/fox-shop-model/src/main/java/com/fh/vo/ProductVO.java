package com.fh.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    private Long id;
    private Long brandId;
    private Long productCategoryId;
    private String name;
    /**
     * 货号
     */
    private String productSn;
    /**
     * 推荐状态；0->不推荐；1->推荐
     */
    private Integer recommandStatus;
    /**
     * 上架状态：0->下架；1->上架
     */
    private Integer publishStatus;
    /**
     * 新品状态:0->不是新品；1->新品
     */
    private Integer newStatus;
    private String pic;
    /**
     * 审核状态：0->未审核；1->审核通过
     */
    private Integer verifyStatus;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 销量
     */
    private Integer sale;
    private BigDecimal price;
    private String brandName;
    private String categoryName;

}
