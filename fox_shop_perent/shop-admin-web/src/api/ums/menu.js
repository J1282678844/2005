import request from '../../utils/request';

export const fetchMenuData = query => {
    return request({
        url: '/fox/ums/admin/menu',
        method: 'get',
        params: query
    });
};

export const getMenuById = id => {
    return request({
        url: '/fox/ums/admin/menu/'+id,
        method: 'get'
    });
};

export const saveOrUpdateMenu = params => {
    return request({
        url: '/fox/ums/admin/menu',
        method: 'post',
        params: params
    });
};

export const deleteMenu = id => {
    return request({
        url: '/fox/ums/admin/menu/'+id,
        method: 'delete',
    });
};
