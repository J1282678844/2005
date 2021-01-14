package com.fh.brand.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.brand.entity.PmsBrand;
import com.fh.brand.service.IPmsBrandService;
import com.fh.commons.result.CommonsReturn;
import com.fh.search.BrandSearch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/pms/pmsBrand")
public class PmsBrandController {

    @Resource
    private IPmsBrandService brandService;

    @GetMapping
    public CommonsReturn queryPageBrand(Page<PmsBrand> page, BrandSearch search) {
        QueryWrapper<PmsBrand> queryWrapper=new QueryWrapper<PmsBrand>();
        if(StringUtils.isNotBlank(search.getName())){
            queryWrapper.like("name",search.getName());
        }
        IPage<PmsBrand> iPage = brandService.page(page,queryWrapper);
        return CommonsReturn.success(iPage);
    }

    @PostMapping
    public CommonsReturn saveBrand(PmsBrand brand){
        brandService.saveOrUpdate(brand);
        return CommonsReturn.success();
    }

    @PutMapping("/factory")
    public CommonsReturn updateFactoryStatus(PmsBrand brand){
        brandService.updateById(brand);
        return CommonsReturn.success();
    }
    @PutMapping("/show")
    public CommonsReturn updateShowStatus(PmsBrand brand){
        brandService.updateById(brand);
        return CommonsReturn.success();
    }

    @GetMapping("/{brandId}")
    public CommonsReturn getBrandById( @PathVariable("brandId") Long brandId){
        PmsBrand brand = brandService.getById(brandId);
        return CommonsReturn.success(brand);
    }
    @GetMapping("/all")
    public CommonsReturn  getBrandAll(){
        List<PmsBrand> list = brandService.list();
        return CommonsReturn.success(list);
    }

}
