package org.mimosaframework.core.utils;

import org.mimosaframework.core.json.ModelObject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestUtils {
    public static String getWebUrl(HttpServletRequest request) {
        String protocol = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String webapp = request.getContextPath();

        if (port == 80) {
            return protocol + "://" + host + (StringTools.isNotEmpty(webapp) ? "/" + webapp : "");
        } else {
            return protocol + "://" + host + ":" + port + (StringTools.isNotEmpty(webapp) ? "/" + webapp : "");
        }
    }

    public static String getWebDomain(HttpServletRequest request) {
        String host = request.getServerName();
        int port = request.getServerPort();

        if (port == 80) {
            return host;
        } else {
            return host + ":" + port;
        }
    }

    public static String getCurrentUrl(HttpServletRequest request) {
        String query = request.getQueryString();
        if (StringTools.isNotEmpty(query)) {
            return getWebUrl(request) + request.getRequestURI() + "?" + query;
        } else {
            return getWebUrl(request) + request.getRequestURI();
        }
    }

    /**
     * 需要web.xml中配置org.springframework.web.util.WebAppRootListener
     *
     * @return
     */
    public static String getWebAppRoot() {
        return System.getProperty("webapp.root");
    }

    public static File getWebAppRootFile() {
        return new File(getWebAppRoot());
    }

    public static File getWebAppRoot(String relativeFilePath) {
        return new File(getWebAppRoot() + File.separator + relativeFilePath);
    }

    public static String getUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String query = request.getQueryString();
        if (StringTools.isNotEmpty(query)) {
            url.append("?");
            url.append(query);
        }
        return url.toString();
    }

    public static String redirect(HttpServletRequest request, String url, String pm) {
        url = getWebUrl(request) + url;
        try {
            if (url.indexOf("?") > 0) {
                url += "&" + pm + "=" + URLEncoder.encode(getCurrentUrl(request), "UTF-8");
            } else {
                url += "?" + pm + "=" + URLEncoder.encode(getCurrentUrl(request), "UTF-8");
            }
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> getUrlParams(String url) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
        Map<String, String> map = new HashMap<>();
        if (StringTools.isNotEmpty(params)) {
            String[] s1 = params.split("&");
            for (String s2 : s1) {
                String[] s3 = s2.split("=");
                map.put(s3[0], s3.length == 1 ? "" : s3[1]);
            }
        }
        return map;
    }

    public static String replaceUrlParams(String url, Map<String, String> map) {
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            sb.append(entry.getKey() + "=" + entry.getValue());
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        int index = url.indexOf("?");
        if (index < 0) index = url.length();
        return url.substring(0, index) + "?" + sb.toString();
    }

    //获取客户端的IP地址
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"".equals(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ("").equals(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ("").equals(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ("").equals(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static ModelObject getParamToModelObject(HttpServletRequest request) {
        Enumeration names = request.getParameterNames();
        ModelObject object = new ModelObject();
        while (names.hasMoreElements()) {
            String name = names.nextElement() + "";
            object.put(name, request.getParameter(name));
        }
        return object;
    }
}
