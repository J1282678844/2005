package com.fh.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fh.admin.entity.UmsAdmin;
import com.fh.admin.entity.UserVo;

import javax.annotation.Priority;
import javax.annotation.Resource;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-07
 */
public interface IUmsAdminService extends IService<UmsAdmin> {


    IPage<UserVo> queryPageUser(Page<UserVo> page);
}
