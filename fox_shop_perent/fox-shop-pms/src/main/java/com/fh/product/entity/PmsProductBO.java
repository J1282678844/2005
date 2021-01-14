package com.fh.product.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName PmsProductBO
 * @Author 阳
 * @Date 2020/12/13 19:24
 * @Version 1.0
 **/
@Data
public class PmsProductBO extends PmsProduct {

    //会员价格集合
    private List<PmsMemberPrice> memberPriceList;
    //阶梯价格维护
    private List<PmsProductLadder> productLadderList;
    //满减价格
    private List<PmsProductFullReduction> productFullReductionList;
    //SKU维护
    private List<PmsSkuStock> skuStockList;
    //商品的属性和参数列表
    private List<PmsProductAttributeValue> productAttributeValueList;

}
