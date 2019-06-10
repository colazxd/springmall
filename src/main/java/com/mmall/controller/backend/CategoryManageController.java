package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author colaz
 * @date 2019/5/8
 **/

@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                                     @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getCategory(Integer categoryId) {
        ServerResponse response = iCategoryService.selectParallelChildren(categoryId);
        return response;
    }


    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Integer>> getDeepCategory(Integer categoryId) {
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }

}
