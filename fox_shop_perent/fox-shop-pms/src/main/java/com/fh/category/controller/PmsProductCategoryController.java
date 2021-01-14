package com.fh.category.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.category.entity.PmsProductCategory;
import com.fh.category.service.IPmsProductCategoryService;
import com.fh.commons.result.CommonsReturn;
import com.fh.search.CategorySearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/pms/category")
public class PmsProductCategoryController {

    @Autowired
    private IPmsProductCategoryService categoryService;

    //分页查询
    @GetMapping
    public CommonsReturn queryPageCategory(Page<PmsProductCategory> page, CategorySearch categorySearch){
        QueryWrapper<PmsProductCategory> queryWrapper=new QueryWrapper<PmsProductCategory>();
        if(categorySearch.getPid() != null){
            queryWrapper.eq("parent_Id",categorySearch.getPid());
        }else{
            queryWrapper.eq("level",categorySearch.getLevel());
        }
        if(StringUtils.isNotBlank(categorySearch.getName())){
            queryWrapper.like("name",categorySearch.getName());
        }
        IPage<PmsProductCategory> iPage = categoryService.page(page,queryWrapper);
        return CommonsReturn.success(iPage);
    }

    //查询上下级分类
    @GetMapping("/parent")
    public CommonsReturn queryParentCategoryList(){
        QueryWrapper<PmsProductCategory> queryWrapper=new QueryWrapper<PmsProductCategory>();
        queryWrapper.eq("level",0);
        List<PmsProductCategory> categoryList = categoryService.list(queryWrapper);
        return CommonsReturn.success(categoryList);
    }

    //根据id回显
    @GetMapping("/a")
    public CommonsReturn queryCategoryById(Long id){
        PmsProductCategory category = categoryService.getById(id);
        return CommonsReturn.success(category);
    }

    //新增
    @PostMapping
    public CommonsReturn saveCategory(PmsProductCategory category){
        Long parentId=category.getParentId();
        if(parentId == 0){
            category.setLevel(0);
        }else{
            category.setLevel(1);
        }
        categoryService.saveOrUpdate(category);
        return  CommonsReturn.success();
    }

    @PutMapping("/show")
    public CommonsReturn updateShowStatus(PmsProductCategory category){
        categoryService.updateById(category);
        return CommonsReturn.success();
    }

    @PutMapping("/nav")
    public CommonsReturn updateNavStatus(PmsProductCategory category){
        categoryService.updateById(category);
        return CommonsReturn.success();
    }

    //商品分类查询
    @GetMapping("all")
    public CommonsReturn getCategoryAll(){
        List<Map<String,Object>> list=categoryService.getCategoryAll();
        return CommonsReturn.success(list);
    }

}
