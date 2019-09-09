package org.mimosaframework.core.monitor;

/**
 * 远程发送者的接口，在监控中有可能会使用到远程监控，每一个远程
 * 监控队列都会对应一个远程监控发送者，它们用来实现将消息发送到
 * 指定的服务器或者其他方式
 *
 * @author yangankang
 */
public interface RemoteSender {

    /**
     * 使用http模式发送发送日志消息
     *
     * @see HttpAsyncRemoteSender
     */
    String HTTP_MODE = "http";

    /**
     * 使用tcp模式发送发送日志消息
     *
     * @see TcpAsyncRemoteSender
     */
    String TCP_MODE = "tcp";

    /**
     * 使用udp模式发送日志消息
     */
    String UDP_MODE = "udp";

    /**
     * 给发送者实现的配置参数，ip、端口等等
     *
     * @param setting 配置参数
     */
    void setSetting(RemoteSenderSetting setting);


    /**
     * 发送的消息体
     *
     * @param monitoringCarrier 消息内容
     */
    void sendMonitoring(MonitoringCarrier monitoringCarrier);

}
