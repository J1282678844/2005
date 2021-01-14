import request from '../../utils/request';

export const fetchAttrCategoryData = query => {
    return request({
        url: '/fox/pms/attr/category',
        method: 'get',
        params: query
    });
};

export const getAttrCategoryById = id => {
    return request({
        url: '/fox/pms/attr/category/'+id,
        method: 'get'
    });
};

export const saveOrUpdateAttrCategory = params => {
    return request({
        url: '/fox/pms/attr/category',
        method: 'post',
        params: params
    });
};

export const fetchAttrCategoryAll = () => {
    return request({
        url: '/fox/pms/attr/category/all',
        method: 'get'
    });
};
