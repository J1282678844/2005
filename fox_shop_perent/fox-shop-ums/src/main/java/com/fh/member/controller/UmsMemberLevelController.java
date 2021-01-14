package com.fh.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.commons.result.CommonsReturn;
import com.fh.member.entity.UmsMemberLevel;
import com.fh.member.service.IUmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 会员等级表 前端控制器
 * </p>
 *
 * @author jiaoqiyang
 * @since 2020-12-13
 */
@RestController
@RequestMapping("/ums/member/level")
public class UmsMemberLevelController {

    @Autowired
    private IUmsMemberLevelService memberLevelService;

    @GetMapping("/{defaultStatus}")
    public CommonsReturn queryMemberLevel(@PathVariable("defaultStatus") Integer defaultStatus){
        QueryWrapper<UmsMemberLevel> queryWrapper=new QueryWrapper<UmsMemberLevel>();
        queryWrapper.eq("default_status",defaultStatus);
        List<UmsMemberLevel> memberLevelList=memberLevelService.list(queryWrapper);
        return CommonsReturn.success(memberLevelList);

    }

}
