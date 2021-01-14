package com.fh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.admin.entity.UmsAdmin;
import com.fh.admin.entity.UserVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-07
 */
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {

    IPage<UserVo> queryPageUser(@Param("page") Page<UserVo> page);
}
