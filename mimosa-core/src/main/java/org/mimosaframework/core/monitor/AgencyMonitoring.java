package org.mimosaframework.core.monitor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 当前类是将多个监控合并在一起，比如本地日志和远程发送监控可以放到一起,
 * 这里不是线程安全的,本类的所有方法都是代理方法
 *
 * @author yangankang
 */
public class AgencyMonitoring implements Monitoring {

    /**
     * 使用的来源类
     */
    private Class<?> clazz = null;

    /**
     * 所有监控集合
     */
    private List<Monitoring> monitorings = new CopyOnWriteArrayList<Monitoring>();


    /**
     * 是否发送某一个类型的日志
     */
    private boolean isDebugEnabled = true;
    private boolean isErrorEnabled = true;
    private boolean isFatalEnabled = true;
    private boolean isTraceEnabled = true;
    private boolean isInfoEnabled = true;
    private boolean isWarnEnabled = true;

    public AgencyMonitoring(Class<?> clazz, CopyOnWriteArrayList<Monitoring> monitorings) {
        this.clazz = clazz;
        this.monitorings = monitorings;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param fromClass 使用到监控的类
     */
    @Override
    public void setFromClass(Class fromClass) {
        for (Monitoring monitoring : monitorings) {
            monitoring.setFromClass(fromClass);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param is
     */
    @Override
    public void setErrorEnabled(boolean is) {
        this.isErrorEnabled = is;
        for (Monitoring monitoring : monitorings) {
            monitoring.setErrorEnabled(is);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param is
     */
    @Override
    public void setFatalEnabled(boolean is) {
        this.isFatalEnabled = is;
        for (Monitoring monitoring : monitorings) {
            monitoring.setFatalEnabled(is);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param is
     */
    @Override
    public void setInfoEnabled(boolean is) {
        this.isInfoEnabled = is;
        for (Monitoring monitoring : monitorings) {
            monitoring.setInfoEnabled(is);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param is
     */
    @Override
    public void setTraceEnabled(boolean is) {
        this.isTraceEnabled = is;
        for (Monitoring monitoring : monitorings) {
            monitoring.setTraceEnabled(is);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param is
     */
    @Override
    public void setWarnEnabled(boolean is) {
        this.isWarnEnabled = is;
        for (Monitoring monitoring : monitorings) {
            monitoring.setWarnEnabled(is);
        }
    }

    @Override
    public void setDebugEnabled(boolean is) {
        this.isDebugEnabled = is;
        for (Monitoring monitoring : monitorings) {
            monitoring.setWarnEnabled(is);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     */
    @Override
    public void debug(Object message) {
        for (Monitoring monitoring : monitorings) {
            monitoring.debug(message);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     * @param t
     */
    @Override
    public void debug(Object message, Throwable t) {
        for (Monitoring monitoring : monitorings) {
            monitoring.debug(message, t);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     */
    @Override
    public void error(Object message) {
        for (Monitoring monitoring : monitorings) {
            monitoring.error(message);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     * @param t
     */
    @Override
    public void error(Object message, Throwable t) {
        for (Monitoring monitoring : monitorings) {
            monitoring.error(message, t);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     */
    @Override
    public void fatal(Object message) {
        for (Monitoring monitoring : monitorings) {
            monitoring.fatal(message);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     * @param t
     */
    @Override
    public void fatal(Object message, Throwable t) {
        for (Monitoring monitoring : monitorings) {
            monitoring.fatal(message, t);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     */
    @Override
    public void info(Object message) {
        for (Monitoring monitoring : monitorings) {
            monitoring.info(message);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     * @param t
     */
    @Override
    public void info(Object message, Throwable t) {
        for (Monitoring monitoring : monitorings) {
            monitoring.info(message, t);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @return
     */
    @Override
    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @return
     */
    @Override
    public boolean isErrorEnabled() {
        return isErrorEnabled;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @return
     */
    @Override
    public boolean isFatalEnabled() {
        return isFatalEnabled;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @return
     */
    @Override
    public boolean isInfoEnabled() {
        return isInfoEnabled;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @return
     */
    @Override
    public boolean isTraceEnabled() {
        return isTraceEnabled;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @return
     */
    @Override
    public boolean isWarnEnabled() {
        return isWarnEnabled;
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     */
    @Override
    public void trace(Object message) {
        for (Monitoring monitoring : monitorings) {
            monitoring.trace(message);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     * @param t
     */
    @Override
    public void trace(Object message, Throwable t) {
        for (Monitoring monitoring : monitorings) {
            monitoring.trace(message, t);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     */
    @Override
    public void warn(Object message) {
        for (Monitoring monitoring : monitorings) {
            monitoring.warn(message);
        }
    }

    /**
     * 代理方法 {@link Monitoring}
     *
     * @param message
     * @param t
     */
    @Override
    public void warn(Object message, Throwable t) {
        for (Monitoring monitoring : monitorings) {
            monitoring.warn(message, t);
        }
    }
}
