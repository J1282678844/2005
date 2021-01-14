import request from '../../utils/request';

export const fetchRoleData = query => {
    return request({
        url: '/fox/ums/admin/role',
        method: 'get',
        params: query
    });
};

export const getRoleById = id => {
    return request({
        url: '/fox/ums/admin/role/'+id,
        method: 'get'
    });
};

export const saveOrUpdateRole = params => {
    return request({
        url: '/fox/ums/admin/role',
        method: 'post',
        params: params
    });
};

export const deleteRole = id => {
    return request({
        url: '/fox/ums/admin/role/'+id,
        method: 'delete',
    });
};
