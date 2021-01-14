import request from '../../utils/request';

export const fetchProductData = query => {
    return request({
        url: '/fox/pms/product/product',
        method: 'get',
        params: query
    });
};

export const saveOrUpdateProductData = params => {
  return request({
    url: '/fox/pms/product/product',
    method: 'post',
    data: params
  });
};

export const getProductById = id => {
    return request({
        url: '/fox/pms/product/product/'+id,
        method: 'get'
    });
};
