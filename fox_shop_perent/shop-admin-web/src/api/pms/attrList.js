import request from '../../utils/request';

export const fetchAttrListData = query => {
    return request({
        url: '/fox/pms/attr/attribute',
        method: 'get',
        params: query
    });
};

export const getAttrListById = id => {
    return request({
        url: '/fox/pms/attr/attribute/'+id,
        method: 'get'
    });
};

export const saveOrUpdateAttrList = params => {
    return request({
        url: '/fox/pms/attr/attribute',
        method: 'post',
        params: params
    });
};
