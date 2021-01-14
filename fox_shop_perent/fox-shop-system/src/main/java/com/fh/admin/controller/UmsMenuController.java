package com.fh.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.admin.entity.UmsMenu;
import com.fh.admin.service.IUmsMenuService;
import com.fh.commons.result.CommonsReturn;
import com.fh.search.MenuSearch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/admin/menu")
public class UmsMenuController {

    @Resource
    private IUmsMenuService menuService;

    //分页查询角色数据
    @GetMapping
    public CommonsReturn queryMenu(Page<UmsMenu> page, MenuSearch menuSearch) {
        QueryWrapper<UmsMenu> queryWrapper = new QueryWrapper<>();
        if (menuSearch.getPid() != null) {
            queryWrapper.eq("parent_Id", menuSearch.getPid());
        } else {
            queryWrapper.eq("level", menuSearch.getLevel());
        }
        IPage<UmsMenu> iPage = menuService.page(page, queryWrapper);
        return CommonsReturn.success(iPage);
    }

    //根据ID回显
    @GetMapping("/{id}")
    public CommonsReturn getMenuById(@PathVariable("id") Long id) {
        UmsMenu menu = menuService.getById(id);
        return CommonsReturn.success(menu);
    }

    //新增+修改
    @PostMapping
    public CommonsReturn saveOrUpdateMenu(UmsMenu menu) {
        menuService.saveOrUpdate(menu);
        return CommonsReturn.success();
    }

    //删除角色
    @DeleteMapping("/{id}")
    public CommonsReturn deleteMenu(@PathVariable("id") Long id) {
        menuService.removeById(id);
        return CommonsReturn.success();
    }

}
