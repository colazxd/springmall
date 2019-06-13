package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author colaz
 * @date 2019/5/5
 **/

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(HttpServletRequest request, Product product) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.saveOrUpdate(product);
        }
        return ServerResponse.createByErrorMsg("无权限操作");
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.setStatus(productId, status);
        }
        return ServerResponse.createByErrorMsg("无权限操作");
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpServletRequest request,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.getProductList(pageNum, pageSize);
        }
        return ServerResponse.createByErrorMsg("无权限操作");
    }

    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpServletRequest request,String productName,Integer productId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.searchByProductNameAndId(productName, productId, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMsg("无权限操作");
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest re,
                                      @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                      HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            Map<String, String> resMap = Maps.newHashMap();

            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resMap.put("uri", targetFileName);
            resMap.put("url", url);
            return ServerResponse.createBySuccess(resMap);
        }
        return ServerResponse.createByErrorMsg("无权限操作");

    }









}
