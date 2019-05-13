package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @author colaz
 * @date 2019/5/3
 **/

public interface IProductService {

    ServerResponse saveOrUpdate(Product product);
    ServerResponse<PageInfo> getProductList(String keyword, Integer categoryId,
                                            int pageNum, int pageSize, String orderBy);
}
