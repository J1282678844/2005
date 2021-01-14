package com.fh.product.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.brand.entity.PmsBrand;
import com.fh.brand.service.IPmsBrandService;
import com.fh.category.entity.PmsProductCategory;
import com.fh.category.service.IPmsProductCategoryService;
import com.fh.commons.result.CommonsReturn;
import com.fh.product.entity.PmsProduct;
import com.fh.product.entity.PmsProductBO;
import com.fh.product.service.IPmsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-13
 */
@RestController
@RequestMapping("/pms/product/product")
public class PmsProductController {

    @Autowired
    private IPmsProductService productService;
    @Autowired
    private IPmsBrandService brandService;
    @Autowired
    private IPmsProductCategoryService categoryService;

    //分页查询商品列表
    @GetMapping
    public CommonsReturn queryPageProduct(Page<PmsProduct> page) {
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<PmsProduct>();
        queryWrapper.orderByDesc("id");
        IPage<PmsProduct> iPage = productService.page(page, queryWrapper);
        return CommonsReturn.success(iPage);

    }

    //新增+修改商品
    @PostMapping
    public CommonsReturn saveOrUpdateProductData(@RequestBody PmsProductBO pmsProductBO) {
        productService.createProduct(pmsProductBO);
        return CommonsReturn.success();
    }

    //回显商品数据
    @GetMapping("/{id}")
    private CommonsReturn getProductById(@PathVariable("id") Long id){
        PmsProductBO pmsProductBO = productService.getProductById(id);
        return CommonsReturn.success(pmsProductBO);
    }

}
