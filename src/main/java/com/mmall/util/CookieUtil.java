package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author colaz
 * @date 2019/5/27
 **/
@Slf4j
public class CookieUtil {

    // Cookie域设置为一级域名，子域名aaa.happymmall.com也可以获得，同级域名不能获得
    private static final String COOKIE_DOMAIN = ".happymmal.com";
    private static final String COOKIE_NAME = "mmall_login_token";


    public static void writeLoginCookie(HttpServletResponse response, String token) {
        //
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");        //代表设置在根目录

        ck.setMaxAge(60 * 60 * 24 * 365);   //maxAge不设置，cookie不会写入硬盘。只在当前页面有效
        log.info("write cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }

    public static String readLoginCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie ck : cookies) {
                log.info("read cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    log.info("return cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);    //设置成0代表删除，-1代表永久
                    log.info("del cookieName:{}, CookieValue:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                }
            }
        }
    }



}
