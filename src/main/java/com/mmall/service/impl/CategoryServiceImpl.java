package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author colaz
 * @date 2019/5/8
 **/
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0)
            return ServerResponse.createBySuccessMsg("添加品类成功");
        return ServerResponse.createByErrorMsg("添加品类失败");
    }

    @Override
    public ServerResponse updateCategory(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("修改品类名参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0)
            return ServerResponse.createBySuccessMsg("修改品类名成功");
        return ServerResponse.createByErrorMsg("修改品类名失败");
    }

    @Override
    public ServerResponse<List<Category>> selectParallelChildren(Integer categoryId) {
        List<Category> categories = categoryMapper.selectChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            return ServerResponse.createByErrorMsg("该分类无子分类");
        }
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        HashSet<Category> categorySet = new HashSet<>();
        findChildren(categoryId, categorySet);
        List<Integer> categoryIds = new ArrayList<>();
        if (categoryId != null) {   //
            for(Category category:categorySet) {
                categoryIds.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIds);
    }

    private void findChildren(Integer categoryId, HashSet<Category> categorySet) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null)
            categorySet.add(category);

        List<Category> children = categoryMapper.selectChildrenByParentId(categoryId);
        for(Category child : children) {
            findChildren(child.getId(), categorySet);
        }

        return;
    }
}
