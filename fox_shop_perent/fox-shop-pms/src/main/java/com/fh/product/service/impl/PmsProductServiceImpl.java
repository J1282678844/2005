package com.fh.product.service.impl;

import com.aliyun.oss.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.brand.entity.PmsBrand;
import com.fh.brand.service.IPmsBrandService;
import com.fh.category.entity.PmsProductCategory;
import com.fh.category.service.IPmsProductCategoryService;
import com.fh.product.entity.*;
import com.fh.product.mapper.PmsProductMapper;
import com.fh.product.service.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-13
 */
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements IPmsProductService {

    @Resource
    private IPmsMemberPriceService memberPriceService;
    @Resource
    private IPmsProductLadderService ladderService;
    @Resource
    private IPmsProductFullReductionService fullReductionService;
    @Resource
    private IPmsSkuStockService skuStockService;
    @Resource
    private IPmsProductAttributeValueService attributeValueService;
    @Resource
    private IPmsBrandService brandService;
    @Resource
    private IPmsProductCategoryService categoryService;
    @Override
    @Transactional
    public void createProduct(PmsProductBO pmsProductBO) {
        PmsProduct product = pmsProductBO;
        if(product.getBrandId() != null){
            PmsBrand brand = brandService.getById(product.getBrandId());
            product.setBrandName(brand.getName());
        }
        if(product.getProductCategoryId() != null){
            PmsProductCategory category = categoryService.getById(product.getProductCategoryId());
            product.setProductCategoryName(category.getName());
        }
        boolean status = super.saveOrUpdate(product);
        if (status){
            Long productId = product.getId();
            //保存会员价格
            saveMemberPrice(pmsProductBO.getMemberPriceList(),productId);
            //保存阶梯价格
            saveProductLadder(pmsProductBO.getProductLadderList(),productId);
            //保存满减价格
            saveFullReduction(pmsProductBO.getProductFullReductionList(),productId);
            //SKU维护
            saveSkuStock(pmsProductBO.getSkuStockList(),productId);
            //保存商品的属性和参数列表
            saveProductAttributeValue(pmsProductBO.getProductAttributeValueList(),productId);

        }
    }

    //回显商品数据
    @Override
    public PmsProductBO getProductById(Long id) {
        PmsProductBO pmsProductBO = new PmsProductBO();
        BeanUtils.copyProperties(super.getById(id),pmsProductBO);
        Map<String,Object> map = new HashMap<>();
        map.put("product_id",id);
        //保存会员价格
        List<PmsMemberPrice> pmsMemberPrices = memberPriceService.listByMap(map);
        //保存阶梯价格
        List<PmsProductLadder> pmsProductLadders = ladderService.listByMap(map);
        //保存满减价格
        List<PmsProductFullReduction> pmsProductFullReductions = fullReductionService.listByMap(map);
        //SKU维护
        List<PmsSkuStock> pmsSkuStocks = skuStockService.listByMap(map);
        //保存商品的属性和参数列表
        List<PmsProductAttributeValue> pmsProductAttributeValues = attributeValueService.listByMap(map);
        pmsProductBO.setMemberPriceList(pmsMemberPrices);
        pmsProductBO.setProductAttributeValueList(pmsProductAttributeValues);
        pmsProductBO.setProductFullReductionList(pmsProductFullReductions);
        pmsProductBO.setProductLadderList(pmsProductLadders);
        pmsProductBO.setSkuStockList(pmsSkuStocks);
        return pmsProductBO;
    }

    //保存商品的属性和参数列表
    private void saveProductAttributeValue(List<PmsProductAttributeValue> productAttributeValueList, Long productId) {
        productAttributeValueList.forEach(pmsProductAttributeValue -> {
            pmsProductAttributeValue.setProductId(productId);
        });
        attributeValueService.remove(new QueryWrapper<PmsProductAttributeValue>().eq("product_id",productId));
        attributeValueService.saveBatch(productAttributeValueList);
    }

    //SKU维护
    private void saveSkuStock(List<PmsSkuStock> skuStockList, Long productId) {
        for (int i=0;i<skuStockList.size();i++){
            PmsSkuStock pmsSkuStock=skuStockList.get(i);
            pmsSkuStock.setProductId(productId);
            String dateStr= DateFormatUtils.format(new Date(),"yyyyMMdd");
            String productCode=String.format("%06d",productId);
            String skuCode=String.format("%03d",i+1);
            pmsSkuStock.setSkuCode(StringUtils.join(dateStr,productCode,skuCode));
        }
        skuStockService.remove(new QueryWrapper<PmsSkuStock>().eq("product_id",productId));
        skuStockService.saveBatch(skuStockList);
    }

    //保存满减价格
    private void saveFullReduction(List<PmsProductFullReduction> productFullReductionList, Long productId) {
        productFullReductionList.forEach(pmsProductFullReduction -> {
            pmsProductFullReduction.setProductId(productId);
        });
        fullReductionService.remove(new QueryWrapper<PmsProductFullReduction>().eq("product_id",productId));
        fullReductionService.saveBatch(productFullReductionList);
    }

    //保存阶梯价格
    private void saveProductLadder(List<PmsProductLadder> productLadderList, Long productId) {
        productLadderList.forEach(ladder->{
            ladder.setProductId(productId);
        });
        ladderService.remove(new QueryWrapper<PmsProductLadder>().eq("product_id",productId));
        ladderService.saveBatch(productLadderList);
    }

    //保存会员价格
    private void saveMemberPrice(List<PmsMemberPrice> list,Long productId){
        list.forEach(memberPrice->{
            memberPrice.setProductId(productId);
        });
        memberPriceService.remove(new QueryWrapper<PmsMemberPrice>().eq("product_id",productId));
        memberPriceService.saveBatch(list);
    }


}
