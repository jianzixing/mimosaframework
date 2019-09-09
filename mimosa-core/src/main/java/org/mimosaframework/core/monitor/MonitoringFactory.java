package org.mimosaframework.core.monitor;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 获得监控类的工厂，这个工厂很简单，就是返回一个MonitoringQueue的实例
 *
 * @author yangankang
 */
public class MonitoringFactory {

    /**
     * 全局唯一的，用来装载同时运行的多个监控器
     */
    private static final CopyOnWriteArrayList<Monitoring> monitorings = new CopyOnWriteArrayList<Monitoring>();

    /**
     * 远程控制器必须的发送者 {@link RemoteMonitoringQueue}
     */
    private static RemoteSender sender = null;

    private static boolean isDebugEnabled = true;
    private static boolean isErrorEnabled = true;
    private static boolean isFatalEnabled = true;
    private static boolean isTraceEnabled = true;
    private static boolean isInfoEnabled = true;
    private static boolean isWarnEnabled = true;

    /**
     * 获得一个多功能的监控器
     *
     * @param clazz 使用的类
     * @return
     */
    public static AgencyMonitoring getMonitoring(Class<?> clazz) {
        monitorings.add(new LogMonitoring());
        if (sender != null) {
            monitorings.add(new RemoteMonitoringQueue(sender));
        }
        AgencyMonitoring combineMonitoring = new AgencyMonitoring(clazz, monitorings);
        combineMonitoring.setDebugEnabled(isDebugEnabled);
        combineMonitoring.setErrorEnabled(isErrorEnabled);
        combineMonitoring.setFatalEnabled(isFatalEnabled);
        combineMonitoring.setTraceEnabled(isTraceEnabled);
        combineMonitoring.setInfoEnabled(isInfoEnabled);
        combineMonitoring.setWarnEnabled(isWarnEnabled);
        return combineMonitoring;
    }

    /**
     * 设置一个发送者，远程发送必须一个发送者来支持
     *
     * @param sender
     */
    public static void setSender(RemoteSender sender) {
        MonitoringFactory.sender = sender;
    }

    /**
     * 添加一个额外的监控器
     *
     * @param monitoring
     */
    public static void addMonitor(Monitoring monitoring) {
        monitorings.add(monitoring);
    }

    /**
     * 将配置文件中设置的各种等级是否启用的配置赋值
     *
     * @param isDebugEnabled
     * @param isErrorEnabled
     * @param isFatalEnabled
     * @param isTraceEnabled
     * @param isInfoEnabled
     * @param isWarnEnabled
     */
    public static void setMonitorEnable(boolean isDebugEnabled, boolean isErrorEnabled, boolean isFatalEnabled,
                                        boolean isTraceEnabled, boolean isInfoEnabled, boolean isWarnEnabled) {
        MonitoringFactory.isDebugEnabled = isDebugEnabled;
        MonitoringFactory.isErrorEnabled = isErrorEnabled;
        MonitoringFactory.isFatalEnabled = isFatalEnabled;
        MonitoringFactory.isTraceEnabled = isTraceEnabled;
        MonitoringFactory.isInfoEnabled = isInfoEnabled;
        MonitoringFactory.isWarnEnabled = isWarnEnabled;
    }
}
