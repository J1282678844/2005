import request from '../../utils/request';

export const fetchResourceData = query => {
    return request({
        url: '/fox/ums/admin/resource',
        method: 'get',
        params: query
    });
};

export const getResourceById = id => {
    return request({
        url: '/fox/ums/admin/resource/'+id,
        method: 'get'
    });
};

export const saveOrUpdateResource = params => {
    return request({
        url: '/fox/ums/admin/resource',
        method: 'post',
        params: params
    });
};

export const deleteResource = id => {
    return request({
        url: '/fox/ums/admin/resource/'+id,
        method: 'delete',
    });
};
