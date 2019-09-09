package org.mimosaframework.core.monitor;

/**
 * 远程监控发送者需要的配置，包含IP、端口等等
 *
 * @author yangankang
 * @see HttpAsyncRemoteSender
 * @see TcpAsyncRemoteSender
 */
public class RemoteSenderSetting {

    /**
     * 远程连接的host信息，也可以是一个IP信息
     */
    private String host;

    /**
     * 远程连接的端口信息，默认是80端口
     */
    private int port = 80;

    /**
     * 获取host信息，域名或者ip信息
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置host信息，域名或者ip信息
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获得端口信息
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置端口信息
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }
}
