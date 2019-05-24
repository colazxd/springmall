package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @author colaz
 * @date 2019/5/3
 **/

public interface IProductService {

    // portal


    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId,
                                                         int pageNum, int pageSize, String orderBy);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getDetail(Integer productId);

    // backend
    ServerResponse saveOrUpdate(Product product);

    ServerResponse<String> setStatus(Integer productId, Integer status);

    ServerResponse<PageInfo> searchByProductNameAndId(String productName, Integer productId, int pageNum, int pageSize);
}
