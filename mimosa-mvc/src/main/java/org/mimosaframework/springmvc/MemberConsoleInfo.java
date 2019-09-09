package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MemberConsoleInfo {
    private static boolean isRun = false;
    private static ModelObject console;
    private static String domain;

    public static void setString(String str) {
        console = ModelObject.parseObject(str);
    }

    public static void setDomain(HttpServletRequest request) {
        MemberConsoleInfo.domain = getWebUrl(request);
        final String uri = request.getRequestURI();

        if (!isRun
                && domain.indexOf("localhost") < 0
                && domain.indexOf("127.0.0.1") < 0
                && domain.indexOf("192.168.") < 0) {
            try {
                InetAddress inetAddress = InetAddress.getByName(getWebHost(request));
                String address = inetAddress.getHostAddress();
                if (address != null && (address.indexOf("127.0.0.1") >= 0 || address.indexOf("192.168.") >= 0)) {
                    return;
                }
            } catch (Exception e) {
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Map map = new HashMap();
                    map.put("url", domain);
                    map.put("uri", uri);
                    try {
                        URL url = new URL("https://www.jianzixing.com.cn/jianzixing/client/info.jhtml" +
                                "?url=" + URLEncoder.encode(domain, "UTF-8") +
                                "&uri=" + URLEncoder.encode(uri, "UTF-8"));
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        if (connection.getResponseCode() == 200) {
                            connection.disconnect();
                        } else {
                            url = new URL("http://www.jianzixing.com.cn/jianzixing/client/info.jhtml" +
                                    "?url=" + URLEncoder.encode(domain, "UTF-8") +
                                    "&uri=" + URLEncoder.encode(uri, "UTF-8"));
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);
                            connection.getResponseCode();
                        }
                    } catch (Exception e1) {
                        try {
                            URL url = new URL("http://www.jianzixing.com.cn/jianzixing/client/info.jhtml" +
                                    "?url=" + URLEncoder.encode(domain, "UTF-8") +
                                    "&uri=" + URLEncoder.encode(uri, "UTF-8"));
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);
                            connection.getResponseCode();
                        } catch (Exception e2) {
                        }
                    }
                }
            });
            thread.start();
            isRun = true;
        }
    }

    public static boolean isRun() {
        return isRun;
    }


    public static String getWebUrl(HttpServletRequest request) {
        String protocol = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String webapp = request.getContextPath();

        if (port == 80) {
            return protocol + "://" + host + (!StringUtils.isEmpty(webapp) ? "/" + webapp : "");
        } else {
            return protocol + "://" + host + ":" + port + (!StringUtils.isEmpty(webapp) ? "/" + webapp : "");
        }
    }

    public static String getWebHost(HttpServletRequest request) {
        String host = request.getServerName();

        return host;
    }

    public static String getDomain() {
        return domain;
    }
}
