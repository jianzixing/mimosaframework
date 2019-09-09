package org.mimosaframework.core.monitor;

import org.mimosaframework.core.utils.StringTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * 使用HTTP请求的方式发送日志消息，接收者可以是tomcat容器或者jetty容器
 * 发送方式使用的是post方式，发送过程是明文发送，消息使用key=value&version=1.0的数据结构
 * 发送的监控类型是安装日志级别划分的
 *
 * @author yangankang
 */
public class HttpAsyncRemoteSender implements RemoteSender {

    /**
     * 远程服务器的配置信息
     */
    private RemoteSenderSetting setting;

    /**
     * http发送者的构造函数，构造函数只有一个且必须传入配置设置
     *
     * @param setting 配置信息
     */
    public HttpAsyncRemoteSender(RemoteSenderSetting setting) {
        this.setting = setting;
    }

    /**
     * 设置配置信息，构造函数传入之后也可以修改当前的配置信息，但不管怎么
     * 修改配置信息都不能为空
     *
     * @param setting 配置参数
     */
    @Override
    public void setSetting(RemoteSenderSetting setting) {
        if (setting != null) this.setting = setting;
    }

    /**
     * 发送监控消息，本方法使用jdk自带的URLConnection类进行发送的，
     * 发送之前先将消息转化为name1=value1&name2=value2 的形式，
     * 然后确定配置信息是正确的才会发送，发送的过程是同步的所以可能会阻塞
     * <p>
     * 一般发送时响应时间为3秒,只管发送不会接收处理任何回应消息
     *
     * @param monitoringCarrier 消息内容
     */
    @Override
    public void sendMonitoring(MonitoringCarrier monitoringCarrier) {

        //把传入的消息发送到服务器
        String message = this.getPostMessage(monitoringCarrier);
        String host = this.setting.getHost();
        if (!StringTools.isEmpty(message) && !StringTools.isEmpty(host)) {

            //如果配置的url不是http开头的加上http协议
            if (!host.startsWith("http")) {
                host = "http://" + host;
            }

            this.sendPost(host, message);
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //不允许耗时太长
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 把要发送的信息转换成http传送的结构
     * key=value&key=value 的结构
     *
     * @param monitoringCarrier
     * @return
     */
    private String getPostMessage(MonitoringCarrier monitoringCarrier) {
        StringBuffer sb = new StringBuffer();
        if (monitoringCarrier.getLevel() != null
                && monitoringCarrier.getMessage() != null) {

            sb.append("method=" + RemoteSender.HTTP_MODE);
            sb.append("&");
            sb.append("level=" + monitoringCarrier.getLevel().name());
            sb.append("&");
            sb.append("message=" + monitoringCarrier.getMessage());
            if (monitoringCarrier.getClazz() != null) {
                sb.append("&");
                sb.append("clazz=" + monitoringCarrier.getClazz().getName());
            }
            if (monitoringCarrier.getThrowable() != null) {
                sb.append("&");
                sb.append("throwable=" + monitoringCarrier.getThrowable().getClass().getName());
                sb.append("&");
                sb.append("throwableMessage=" + monitoringCarrier.getThrowable().getMessage());
            }
            if (monitoringCarrier.getDate() != null) {
                sb.append("&");
                sb.append("date=" + monitoringCarrier.getDate().getTime());
            }
            return sb.toString();
        }
        return null;
    }
}
