package org.mimosaframework.core.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 当前类仅仅是简单的将消息打印到控制台，此方式可以替代日志
 *
 * @author yangankang
 */
public class LogMonitoring implements Monitoring {
    private static final Log logger = LogFactory.getLog(LogMonitoring.class);

    /**
     * 当前的消息作用的哪个类
     */
    private Class fromClass = null;

    /**
     * 获得一个日志类，传入的fromClass是否存在
     *
     * @return common的日志接口实例
     */
    private Log getLogger() {
        if (fromClass != null) {
            return LogFactory.getLog(fromClass);
        }
        return logger;
    }

    /**
     * 打印debug消息
     *
     * @param message 消息内容
     */
    @Override
    public void debug(Object message) {
        this.getLogger().debug(message);
    }

    /**
     * 打印带有异常的debug消息
     *
     * @param message 消息内容
     * @param t       异常实例
     */
    @Override
    public void debug(Object message, Throwable t) {
        this.getLogger().debug(message, t);
    }

    /**
     * 打印一个错误消息
     *
     * @param message 错误消息内容
     */
    @Override
    public void error(Object message) {
        this.getLogger().error(message);
    }

    /**
     * 打印一个带有异常的错误消息
     *
     * @param message 错误消息内容
     * @param t       错误的异常
     */
    @Override
    public void error(Object message, Throwable t) {
        this.getLogger().error(message, t);
    }

    /**
     * 打印一个致命的消息
     *
     * @param message 消息内容
     */
    @Override
    public void fatal(Object message) {
        this.getLogger().fatal(message);
    }

    /**
     * 打印一个带异常的致命消息
     *
     * @param message 消息的内容
     * @param t       异常实例
     */
    @Override
    public void fatal(Object message, Throwable t) {
        this.getLogger().fatal(message, t);
    }

    /**
     * 打印一个提示消息
     *
     * @param message 消息内容
     */
    @Override
    public void info(Object message) {
        this.getLogger().info(message);
    }

    /**
     * 打印一个带异常的提示消息
     *
     * @param message 消息内容
     * @param t       异常实例
     */
    @Override
    public void info(Object message, Throwable t) {
        this.getLogger().info(message, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    /**
     * 判断是否启用错误日志
     *
     * @return boolean值
     */
    @Override
    public boolean isErrorEnabled() {
        return this.getLogger().isErrorEnabled();
    }

    /**
     * 判断是否启用致命日志消息
     *
     * @return boolean值
     */
    @Override
    public boolean isFatalEnabled() {
        return this.getLogger().isFatalEnabled();
    }

    /**
     * 判断是否启用提示消息
     *
     * @return boolean值
     */
    @Override
    public boolean isInfoEnabled() {
        return this.getLogger().isInfoEnabled();
    }

    /**
     * 判断是否启用跟踪消息
     *
     * @return boolean值
     */
    @Override
    public boolean isTraceEnabled() {
        return this.getLogger().isTraceEnabled();
    }

    /**
     * 判断是否启用警告消息
     *
     * @return boolean值
     */
    @Override
    public boolean isWarnEnabled() {
        return this.getLogger().isWarnEnabled();
    }

    /**
     * 打印跟踪日志的消息
     *
     * @param message 消息内容
     */
    @Override
    public void trace(Object message) {
        this.getLogger().trace(message);
    }

    /**
     * 打印带异常的跟踪日志消息
     *
     * @param message 消息内容
     * @param t       异常实例
     */
    @Override
    public void trace(Object message, Throwable t) {
        this.getLogger().trace(message, t);
    }

    /**
     * 打印一个警告消息
     *
     * @param message 消息的内容
     */
    @Override
    public void warn(Object message) {
        this.getLogger().warn(message);
    }

    /**
     * 打印一个带异常的警告消息
     *
     * @param message 消息的内容
     * @param t       异常实例
     */
    @Override
    public void warn(Object message, Throwable t) {
        this.getLogger().warn(message, t);
    }

    /**
     * 设置监控的类
     *
     * @param fromClass 使用到监控的类
     */
    @Override
    public void setFromClass(Class fromClass) {
        this.fromClass = fromClass;
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setErrorEnabled(boolean is) {
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setFatalEnabled(boolean is) {
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setInfoEnabled(boolean is) {
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setTraceEnabled(boolean is) {
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setWarnEnabled(boolean is) {
    }

    @Override
    public void setDebugEnabled(boolean is) {

    }
}
