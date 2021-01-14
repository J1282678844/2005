import request from '../../utils/request';

export const loginSubmit = query => {
    return request({
        url: '/fox/login',
        method: 'post',
        params: query
    });
};
