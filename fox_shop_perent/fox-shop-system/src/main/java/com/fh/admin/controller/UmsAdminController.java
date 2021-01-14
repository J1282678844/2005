package com.fh.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.admin.entity.UmsAdmin;
import com.fh.admin.service.IUmsAdminService;
import com.fh.commons.result.CommonsReturn;
import com.fh.commons.utils.MD5Util;
import com.fh.search.UserSearch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-07
 */
@RestController
@RequestMapping("/ums/admin/user")
public class UmsAdminController {

    @Resource
    private IUmsAdminService umsAdminService;

    //分页查询用户数据
    @GetMapping
    public CommonsReturn queryAdmin(Page<UmsAdmin> page, UserSearch userSearch) {
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userSearch.getUsername())) {
            queryWrapper.like("username", userSearch.getUsername());
        }
        IPage<UmsAdmin> iPage = umsAdminService.page(page, queryWrapper);
        return CommonsReturn.success(iPage);
    }

    //根据ID回显
    @GetMapping("/{id}")
    public CommonsReturn getUserById(@PathVariable("id") Long id) {
        UmsAdmin admin = umsAdminService.getById(id);
        return CommonsReturn.success(admin);
    }

    //新增+修改
    @PostMapping
    public CommonsReturn saveOrUpdateUser(UmsAdmin admin) {
        if (admin.getId() == null) {
            admin.setCreateTime(new Date());
            admin.setPassword(MD5Util.getMD5("123456"));
        }
        umsAdminService.saveOrUpdate(admin);
        return CommonsReturn.success();
    }

    //删除用户
    @DeleteMapping("/{id}")
    public CommonsReturn deleteUser(@PathVariable("id") Long id) {
        umsAdminService.removeById(id);
        return CommonsReturn.success();
    }


}
