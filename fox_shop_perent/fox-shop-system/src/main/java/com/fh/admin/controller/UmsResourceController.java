package com.fh.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.admin.entity.UmsResource;
import com.fh.admin.entity.UmsRole;
import com.fh.admin.service.IUmsResourceService;
import com.fh.commons.result.CommonsReturn;
import com.fh.search.ResourceSearch;
import com.fh.search.RoleSearch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/admin/resource")
public class UmsResourceController {

    @Resource
    private IUmsResourceService resourceService;

    //分页查询资源数据
    @GetMapping
    public CommonsReturn queryResource(Page<UmsResource> page, ResourceSearch resourceSearch) {
        QueryWrapper<UmsResource> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(resourceSearch.getNameKeyword())) {
            queryWrapper.like("name", resourceSearch.getNameKeyword());
        }
        if (StringUtils.isNotBlank(resourceSearch.getUrlKeyword())) {
            queryWrapper.like("url", resourceSearch.getUrlKeyword());
        }
        IPage<UmsResource> iPage = resourceService.page(page, queryWrapper);
        return CommonsReturn.success(iPage);
    }

    //根据ID回显
    @GetMapping("/{id}")
    public CommonsReturn getResourceById(@PathVariable("id") Long id) {
        UmsResource resource = resourceService.getById(id);
        return CommonsReturn.success(resource);
    }

    //新增+修改
    @PostMapping
    public CommonsReturn saveOrUpdateResource(UmsResource resource) {
        if (resource.getId() == null) {
            resource.setCreateTime(new Date());
        }
        resourceService.saveOrUpdate(resource);
        return CommonsReturn.success();
    }

    //删除角色
    @DeleteMapping("/{id}")
    public CommonsReturn deleteResource(@PathVariable("id") Long id) {
        resourceService.removeById(id);
        return CommonsReturn.success();
    }

}
