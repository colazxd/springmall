package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author colaz
 * @date 2019/5/3
 **/
@Service("iProductService")
public class productServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdate(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {   //取第一个子图作为主图
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {      //update
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if (rowCount > 0)
                    return ServerResponse.createBySuccess("修改产品成功");
                else
                    return ServerResponse.createBySuccess("修改产品失败");
            } else {    //insert
                int rowCount = productMapper.insert(product);
                if (rowCount > 0)
                    return ServerResponse.createBySuccess("新增产品成功");
                else
                    return ServerResponse.createBySuccess("新增产品失败");
            }

        }
        return ServerResponse.createByErrorMsg("参数错误");
    }

    public ServerResponse<PageInfo> getProductList(String keyword, Integer categoryId,
                                         int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectProductList(keyword, categoryId);

        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product product:productList) {
            productListVos.add(assmbleProductVo(product));
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);


    }

    private ProductListVo assmbleProductVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }
}
