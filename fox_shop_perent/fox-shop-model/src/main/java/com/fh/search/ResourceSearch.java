package com.fh.search;

import lombok.Data;

/**
 * @ClassName UserSearch
 * @Author 阳
 * @Date 2020/12/17 11:53
 * @Version 1.0
 **/
@Data
public class ResourceSearch {

    private String nameKeyword;
    private String urlKeyword;
    private Long categoryId;

}
