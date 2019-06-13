package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author colaz
 * @date 2019/6/13
 **/
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        //请求中Controller的方法名
        HandlerMethod handlerMethod = (HandlerMethod)o;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //参数解析，具体的参数key和value是什么
        StringBuffer paramBuffer = new StringBuffer();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Iterator iterator = parameterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = StringUtils.EMPTY;

            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[])obj;
                value = Arrays.toString(strs);
            }
            paramBuffer.append(key).append("=").append(value);
        }

        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")) {
            log.info("权限拦截器拦截到请求：className：{}，method：{}", className, methodName);
            //转到controller
            return true;
        }

        User user = null;
        String loginToken = CookieUtil.readLoginCookie(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
        }
        // 对用户未登录或者无权限进行拦截
        if (user == null || user.getRole() != Const.Role.ROLE_ADMIN) {
            httpServletResponse.reset();        //不reset会报异常： getWriter() has already been called for this response;
            httpServletResponse.setCharacterEncoding("UTF-8");  //设置编码，否则会乱码
            httpServletResponse.setContentType("application/json;charset=UTF-8");  //设置返回类型
            PrintWriter out = httpServletResponse.getWriter();
            if (user == null) {
                //定制富文本返回值
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")) {
                    log.info("权限拦截器拦截到请求：className：{}，method：{}", className, methodName);
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMsg("拦截器拦截，用户未登录")));
                }
            } else {
                //定制富文本返回值
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")) {
                    log.info("权限拦截器拦截到请求：className：{}，method：{}", className, methodName);
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMsg("拦截器拦截，用户无权限")));
                }
            }
            out.flush();
            out.close();    //关闭

            //不进入Controller
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
