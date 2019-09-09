package org.mimosaframework.core.monitor;

import java.util.Date;

/**
 * 消息携带者，供给{@see org.mimosaframework.core.monitor.RemoteSender}
 * 发送消息使用的Bean
 */
public class MonitoringCarrier {
    /**
     * 监控的等级，和日志等级是一样的
     */
    private MonitoringLevel level = null;

    /**
     * 消息的内容
     */
    private Object message = null;

    /**
     * 消息来源的类
     */
    private Class clazz = null;

    /**
     * 消息的异常说明
     */
    private Throwable throwable = null;

    /**
     * 日志产生的时间
     */
    private Date date = null;

    public MonitoringLevel getLevel() {
        return level;
    }

    public void setLevel(MonitoringLevel level) {
        this.level = level;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
