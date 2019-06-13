package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author colaz
 * @date 2019/5/16
 **/
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //防止横向越权，需要同时检查userId和shippingId
        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<Shipping> search(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "10") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        String loginToken = CookieUtil.readLoginCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if (user == null) {
            ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }









}
