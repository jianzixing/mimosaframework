package org.mimosaframework.core.monitor;

import org.apache.commons.logging.Log;

/**
 * 当前接口数据监控接口的顶级接口，监控模式分很多种，一种是日志监控
 * 就是简单将日志打印或者持久化 {@see org.mimosaframework.core.LogMonitoringQueue}
 * 还有一种是将消息存入队列，异步将队列的消息发送到监控系统
 * {@see org.mimosaframework.core.RemoteMonitoringQueue}
 *
 * @author yangankang
 */
public interface Monitoring extends Log {

    /**
     * 监控消息的来源，可能会用作日志，也可能用作拍错跟踪
     *
     * @param fromClass 使用到监控的类
     */
    void setFromClass(Class fromClass);

    /**
     * 仅仅是这是是否打印某个等级的日志
     *
     * @param is
     * @return
     */
    void setErrorEnabled(boolean is);

    void setFatalEnabled(boolean is);

    void setInfoEnabled(boolean is);

    void setTraceEnabled(boolean is);

    void setWarnEnabled(boolean is);

    void setDebugEnabled(boolean is);
}
