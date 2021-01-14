package com.fh.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.admin.entity.UmsRole;
import com.fh.admin.service.IUmsRoleService;
import com.fh.commons.result.CommonsReturn;
import com.fh.search.RoleSearch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 后台用户角色表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/admin/role")
public class UmsRoleController {

    @Resource
    private IUmsRoleService roleService;

    //分页查询角色数据
    @GetMapping
    public CommonsReturn queryRole(Page<UmsRole> page, RoleSearch roleSearch) {
        QueryWrapper<UmsRole> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(roleSearch.getName())) {
            queryWrapper.like("name", roleSearch.getName());
        }
        IPage<UmsRole> iPage = roleService.page(page, queryWrapper);
        return CommonsReturn.success(iPage);
    }

    //根据ID回显
    @GetMapping("/{id}")
    public CommonsReturn getRoleById(@PathVariable("id") Long id) {
        UmsRole role = roleService.getById(id);
        return CommonsReturn.success(role);
    }

    //新增+修改
    @PostMapping
    public CommonsReturn saveOrUpdateRole(UmsRole role) {
        if (role.getId() == null) {
            role.setCreateTime(new Date());
        }
        roleService.saveOrUpdate(role);
        return CommonsReturn.success();
    }

    //删除角色
    @DeleteMapping("/{id}")
    public CommonsReturn deleteRole(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return CommonsReturn.success();
    }

}
