import request from '../../utils/request';

export const fetchUserData = query => {
    return request({
        url: '/fox/ums/admin/user',
        method: 'get',
        params: query
    });
};

export const getUserById = id => {
    return request({
        url: '/fox/ums/admin/user/'+id,
        method: 'get'
    });
};

export const saveOrUpdateUser = params => {
    return request({
        url: '/fox/ums/admin/user',
        method: 'post',
        params: params
    });
};

export const deleteUser = id => {
    return request({
        url: '/fox/ums/admin/user/'+id,
        method: 'delete',
    });
};
