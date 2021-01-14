package com.fh.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.admin.entity.UmsAdmin;
import com.fh.admin.entity.UserVo;
import com.fh.admin.mapper.UmsAdminMapper;
import com.fh.admin.service.IUmsAdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-07
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements IUmsAdminService {

    @Resource
    private UmsAdminMapper umsAdminMapper;

    @Override
    public IPage<UserVo> queryPageUser(Page<UserVo> page) {
        IPage<UserVo> iPage = umsAdminMapper.queryPageUser(page);
        return iPage;
    }
}
