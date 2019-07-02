package com.mmall.dao;

import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectByNameAndProductId(@Param(value = "productName") String productName,
                                    @Param(value = "productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName, @Param("categoryIdList") List<Integer> categoryIdList);

    // 查询某个产品的库存，返回值必须是Integer，否则当有商品删除，返回值为null
    Integer selectStockByProductId(Integer productId);
}