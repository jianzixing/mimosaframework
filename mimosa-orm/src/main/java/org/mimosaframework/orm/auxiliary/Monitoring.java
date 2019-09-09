package org.mimosaframework.orm.auxiliary;

public interface Monitoring extends AuxiliaryClient {

    /**
     * 监控callback代码的性能
     *
     * @param callback
     */
    void performance(MonitoringCallback callback);

    void info(String msg);

    void info(String msg, Throwable throwable);

    void warn(String msg);

    void warn(String msg, Throwable throwable);

    void error(String msg);

    void error(String msg, Throwable throwable);

    void error(String msg, MonitoringLevel level, Throwable throwable);

    /**
     * 仅仅是发送一个心跳
     */
    void heartbeat();

    void heartbeat(String msg);
}
