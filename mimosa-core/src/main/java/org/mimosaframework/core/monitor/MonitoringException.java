package org.mimosaframework.core.monitor;

/**
 * 监控类所有的异常都用这个类
 *
 * @author yangankang
 */
public class MonitoringException extends Exception {

    /**
     * 仅仅是个构造函数而已
     */
    public MonitoringException() {
        super();
    }

    /**
     * 构造一个字符消息的函数
     *
     * @param message 消息内容
     */
    public MonitoringException(String message) {
        super(message);
    }

    /**
     * 构造一个带有异常消息的函数
     *
     * @param message 消息内容
     * @param cause   异常实例
     */
    public MonitoringException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带有异常的函数
     *
     * @param cause 异常类
     */
    public MonitoringException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个带有异常类的消息函数
     *
     * @param message            消息内容
     * @param cause              消息实例
     * @param enableSuppression
     * @param writableStackTrace
     */
    public MonitoringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
