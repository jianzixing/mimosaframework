package org.mimosaframework.core.monitor;

/**
 * 远程发送模式的工厂，帮助选择发送到远程的方式，有些服务只支持
 * http模式、有些支持tcp模式，这些不统一，但是都是由此工厂初始化
 *
 * @author yangankang
 * @see RemoteSender
 */
public class RemoteSenderFactory {

    /**
     * 创建远程发送的发送者，这里是new出来的
     *
     * @param mode 发送方式
     * @return
     */
    public static RemoteSender getRemoteSender(RemoteSenderSetting setting, String mode) throws MonitoringException {
        if (mode.equalsIgnoreCase(RemoteSender.HTTP_MODE)) {
            return new HttpAsyncRemoteSender(setting);
        } else if (mode.equalsIgnoreCase(RemoteSender.TCP_MODE)) {
            return new TcpAsyncRemoteSender(setting);
        } else if (mode.equalsIgnoreCase(RemoteSender.UDP_MODE)) {
            return new UdpAsyncRemoteSender(setting);
        }

        /**
         * 默认使用http模式发送
         */
        return new HttpAsyncRemoteSender(setting);
    }

    /**
     * 实例一个自定义的远程发送者，通过反射实例化，不能有有参构造函数
     *
     * @param setting 需要的配置
     * @param clazz   类名称
     * @return 返回一个发送者
     * @throws MonitoringException 异常说明
     */
    public static RemoteSender getRemoteSenderByClass(RemoteSenderSetting setting, String clazz) throws MonitoringException {
        Class c = null;
        try {
            c = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new MonitoringException("没有找到远程发送者的类", e);
        }
        if (!c.isAssignableFrom(RemoteSender.class)) {
            throw new MonitoringException();
        }
        try {
            RemoteSender remoteSender = (RemoteSender) c.newInstance();
            remoteSender.setSetting(setting);
            return remoteSender;
        } catch (InstantiationException e) {
            throw new MonitoringException("远程发送者实例化失败了", e);
        } catch (IllegalAccessException e) {
            throw new MonitoringException("远程发送者的类不允许访问", e);
        }
    }
}
