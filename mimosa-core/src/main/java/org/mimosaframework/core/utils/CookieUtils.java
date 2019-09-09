package org.mimosaframework.core.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qinmingtao on 2016/5/3.
 * Desc 操作cookie的工具类
 */
public class CookieUtils {
    /**
     * 添加cookie
     *
     * @param name     cookie的key
     * @param value    cookie的value
     * @param domain   domain
     *                 ＠param  path path
     * @param maxage   最长存活时间 单位为秒
     * @param response
     */
    public static void addCookie(String name, String value, String domain,
                                 int maxage, String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(maxage);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void addCookie(String name, String value, int maxage, String path, HttpServletResponse response) {
        addCookie(name, value, null, maxage, path, response);
    }

    /**
     * 往根下面存一个cookie
     * * @param name cookie的key
     *
     * @param value    cookie的value
     * @param domain   domain
     * @param maxage   最长存活时间 单位为秒
     * @param response
     */
    public static void addCookie(String name, String value, String domain,
                                 int maxage, HttpServletResponse response) {
        addCookie(name, value, domain, maxage, "/", response);
    }

    /**
     * 从cookie值返回cookie值，如果没有返回 null
     *
     * @param request
     * @param name
     * @return cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cooky : cookies) {
            if (cooky.getName().equals(name)) {
                return cooky.getValue();
            }
        }
        return null;
    }

    /**
     * 根据名称获取cookie对象
     *
     * @param request
     * @param name
     * @return cookie对象
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cooky : cookies) {
            if (cooky.getName().equals(name)) {
                return cooky;
            }
        }
        return null;
    }

    /**
     * 清除某个域名下的cookie
     *
     * @param name
     * @param domain
     * @param request
     * @param response
     */
    public static void removeCookie(String name, String domain, HttpServletRequest request, HttpServletResponse response) {
        String cookieVal = getCookieValue(request, name);
        if (cookieVal != null) {
            CookieUtils.addCookie(name, null, domain, 0, response);
        }
    }

    /**
     * 清除根域下的cookie
     *
     * @param name
     * @param request
     * @param response
     */
    public static void removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.removeCookie(name, ".", request, response);
    }

    /**
     * 取客户端 存入header中 Cookie
     *
     * @param pReq
     * @return
     */
    public static String getCookieFromHeader(HttpServletRequest pReq) {
        return pReq.getHeader("Cookie");
    }
}
