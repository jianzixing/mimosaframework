package org.mimosaframework.core.monitor;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;

import java.io.IOException;
import java.net.*;

/**
 * 使用udp方式发送监控日志，UDP发送方式不确保消息送达，但是也是会阻塞的
 * UDP的接收者只需要实现服务器接口就行，传送的数据格式是json格式，每一条消息会
 * 以\r\n为结束符,java端用流接收器readLine就行了，建议使用udp方式，这种方式对于
 * 服务器的资源消耗不大
 *
 * @author yangankang
 */
public class UdpAsyncRemoteSender implements RemoteSender {

    /**
     * 远程服务器的配置信息
     */
    private RemoteSenderSetting setting;

    /**
     * 每一个udp唯一的socket实例
     */
    private DatagramSocket client = null;

    /**
     * udp的接收地址
     */
    private InetAddress addr = null;

    /**
     * udp方式的构造函数，必须设置配置信息
     *
     * @param setting
     */
    public UdpAsyncRemoteSender(RemoteSenderSetting setting) {
        this.setting = setting;
        this.initSocket(setting);
    }

    /**
     * 设置配置信息，构造函数传入之后也可以修改当前的配置信息，但不管怎么
     * 修改配置信息都不能为空
     *
     * @param setting 配置参数
     */
    @Override
    public void setSetting(RemoteSenderSetting setting) {
        if (setting != null) {
            this.setting = setting;
            this.initSocket(setting);
        }
    }

    /**
     * 初始化一个udp客户端实例，每一个DatagramSocket是当前实例中唯一的，
     * 如果配置信息有变化则也会更改DatagramSocket的实例
     *
     * @param setting 配置信息
     */
    private void initSocket(RemoteSenderSetting setting) {
        String host = setting.getHost();
        if (!StringTools.isEmpty(host)) {
            if (client != null) {
                client.close();
                client = null;
            }
            try {
                addr = InetAddress.getByName(host);
                client = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用udp方式发送消息日志，只管发送不会接收处理任何回应消息
     *
     * @param monitoringCarrier 消息内容
     */
    @Override
    public void sendMonitoring(MonitoringCarrier monitoringCarrier) {

        String message = this.getSendMessage(monitoringCarrier);
        int port = this.setting.getPort();
        if (!StringTools.isEmpty(message) && client != null && addr != null) {
            byte[] bytes = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, addr, port);
            try {
                client.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把消息对象转化成一个json格式的数据
     *
     * @param monitoringCarrier
     * @return
     */
    private String getSendMessage(MonitoringCarrier monitoringCarrier) {
        ModelObject object = new ModelObject();
        if (monitoringCarrier.getLevel() != null
                && monitoringCarrier.getMessage() != null) {
            object.put("method", UDP_MODE);
            object.put("level", monitoringCarrier.getLevel().name());
            object.put("message", String.valueOf(monitoringCarrier.getMessage()));
            if (monitoringCarrier.getClazz() != null) {
                object.put("clazz", monitoringCarrier.getClazz().getName());
            }
            if (monitoringCarrier.getThrowable() != null) {
                object.put("throwable", monitoringCarrier.getThrowable().getClass().getName());
                object.put("throwableMessage", monitoringCarrier.getThrowable().getMessage());
            }
            if (monitoringCarrier.getDate() != null) {
                object.put("date", monitoringCarrier.getDate().getTime());
            }
            return object.toJSONString();
        }
        return null;
    }
}
