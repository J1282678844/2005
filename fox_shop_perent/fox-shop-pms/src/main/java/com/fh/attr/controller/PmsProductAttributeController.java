package com.fh.attr.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.attr.entity.PmsProductAttribute;
import com.fh.attr.service.IPmsProductAttributeService;
import com.fh.commons.result.CommonsReturn;
import com.fh.search.AttrSearch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 商品属性参数表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/pms/attr/attribute")
public class PmsProductAttributeController {

    @Resource
    private IPmsProductAttributeService pmsProductAttributeService;

    @GetMapping
    public CommonsReturn queryAttrList(Page<PmsProductAttribute> page, AttrSearch search){
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(search.getName())){
            queryWrapper.like("name",search.getName());
        }
        queryWrapper.eq("product_attribute_category_id",search.getCid());
        queryWrapper.eq("type",search.getType());
        queryWrapper.orderByAsc("sort");
        IPage<PmsProductAttribute> iPage = pmsProductAttributeService.page(page,queryWrapper);
        return CommonsReturn.success(iPage);
    }

    //根据ID查询
    @GetMapping("/{id}")
    public  CommonsReturn getAttrById(@PathVariable("id") Long id){
        PmsProductAttribute attribute = pmsProductAttributeService.getById(id);
        return CommonsReturn.success(attribute);
    }

    //新增+修改
    @PostMapping
    public  CommonsReturn saveOrUpdateAttr(PmsProductAttribute attribute){
        pmsProductAttributeService.saveOrUpdate(attribute);
        return CommonsReturn.success();
    }

}
