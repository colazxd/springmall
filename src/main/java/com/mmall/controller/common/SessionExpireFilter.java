package com.mmall.controller.common;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author colaz
 * @date 2019/5/27
 **/
//重置seesion过期时间的filter
public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String loginToken = CookieUtil.readLoginCookie(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            //判断loginToken是否为空
            //***loginToken不为空，拿ueser信息
            //判断user是否存在session有效期内
            User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
            if (user != null) {
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
