package com.fh.attr.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.attr.entity.PmsProductAttributeCategory;
import com.fh.attr.service.IPmsProductAttributeCategoryService;
import com.fh.commons.result.CommonsReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 产品属性分类表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/pms/attr/category")
public class PmsProductAttributeCategoryController {

    @Resource
    private IPmsProductAttributeCategoryService pmsProductAttributeCategoryService;

    //分页查询
    @GetMapping
    public CommonsReturn queryPageCateList(Page<PmsProductAttributeCategory> page){
        IPage<PmsProductAttributeCategory> iPage = pmsProductAttributeCategoryService.page(page);
        return CommonsReturn.success(iPage);
    }

    //根据ID查询
    @GetMapping("/{id}")
    public CommonsReturn getAttrCategoryById(@PathVariable("id") Long id){
        PmsProductAttributeCategory category = pmsProductAttributeCategoryService.getById(id);
        return CommonsReturn.success(category);
    }

    //新增+修改
    @PostMapping
    public CommonsReturn saveOrUpdateAttrCategory(PmsProductAttributeCategory category){
        pmsProductAttributeCategoryService.saveOrUpdate(category);
        return CommonsReturn.success();
    }

    //查询所有属性分类数据
    @GetMapping("/all")
    public CommonsReturn queryCateListAll(){
        List<PmsProductAttributeCategory> list = pmsProductAttributeCategoryService.list();
        return CommonsReturn.success(list);
    }

}
