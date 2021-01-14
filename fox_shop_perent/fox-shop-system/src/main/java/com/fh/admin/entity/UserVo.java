package com.fh.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fh.admin.entity.UmsAdmin;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName UserVo
 * @Deacription TODO
 * @Author 阳
 * @Date 2020/12/2 12:20
 * @Version 1.0
 **/
@Data
public class UserVo extends UmsAdmin {
    private String provinceName;
    private String cityName;
    private String areaName;
    private String eduName;
    private String deptName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;
}
