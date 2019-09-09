package org.mimosaframework.core.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 当前类积累所有消息到队列，每当队列中有消息时则取出来发送到指定的
 * 服务器，这一切都是异步执行的，每一个实例可以对应不同的发送方式，
 * 比如http，tcp等等 {@see org.mimosaframework.core.monitor.RemoteSenderFactory}
 *
 * @author yangankang
 */
public class RemoteMonitoringQueue implements Monitoring {
    private static final Log logger = LogFactory.getLog(LogMonitoring.class);

    /**
     * 每个实例唯一的发送远程发送者的类
     */
    private RemoteSender sender = null;

    /**
     * 监控的类
     */
    private Class fromClass = null;

    /**
     * 是否发送某一个类型的日志
     */
    private boolean isDebugEnabled = true;
    private boolean isErrorEnabled = true;
    private boolean isFatalEnabled = true;
    private boolean isTraceEnabled = true;
    private boolean isInfoEnabled = true;
    private boolean isWarnEnabled = true;


    /**
     * 一个单一的线程，全局使用
     */
    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    /**
     * 全局唯一的阻塞线程队列，在系统启动后整个程序只有一个线程循环获得队列中的值
     */
    private static final LinkedBlockingQueue<QueueEntry> blockingQueue = new LinkedBlockingQueue();

    static {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        QueueEntry queueEntry = blockingQueue.take();
                        MonitoringCarrier carrier = queueEntry.getMonitoringCarrier();
                        RemoteSender sender = queueEntry.getSender();
                        sender.sendMonitoring(carrier);
                    } catch (InterruptedException e) {
                        logger.error("监控消息队列执行错误", e);
                    }
                }
            }
        });
    }

    /**
     * 队列中存的对象，一个永远不会断的线程会不断的或者队列中的对象并且执行发送
     */
    class QueueEntry {
        private MonitoringCarrier monitoringCarrier = null;
        private RemoteSender sender = null;

        public QueueEntry(MonitoringCarrier monitoringCarrier, RemoteSender sender) {
            this.monitoringCarrier = monitoringCarrier;
            this.sender = sender;
        }

        public MonitoringCarrier getMonitoringCarrier() {
            return monitoringCarrier;
        }

        public void setMonitoringCarrier(MonitoringCarrier monitoringCarrier) {
            this.monitoringCarrier = monitoringCarrier;
        }

        public RemoteSender getSender() {
            return sender;
        }

        public void setSender(RemoteSender sender) {
            this.sender = sender;
        }
    }

    /**
     * 构造本类必须给一个发送者
     *
     * @param sender
     */
    public RemoteMonitoringQueue(RemoteSender sender) {
        this.sender = sender;
    }

    /**
     * 发送一个debug消息
     *
     * @param message 消息内容
     */
    @Override
    public void debug(Object message) {
        if (isDebugEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setLevel(MonitoringLevel.DEBUG);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个带异常的debug消息
     *
     * @param message 消息内容
     * @param t       异常
     */
    @Override
    public void debug(Object message, Throwable t) {
        if (isDebugEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setThrowable(t);
            carrier.setLevel(MonitoringLevel.DEBUG);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个错误消息
     *
     * @param message 消息内容
     */
    @Override
    public void error(Object message) {
        if (isErrorEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setLevel(MonitoringLevel.ERROR);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个带异常的错误消息
     *
     * @param message 消息内容
     * @param t       异常
     */
    @Override
    public void error(Object message, Throwable t) {
        if (isErrorEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setThrowable(t);
            carrier.setLevel(MonitoringLevel.ERROR);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个致命的消息
     *
     * @param message 消息内容
     */
    @Override
    public void fatal(Object message) {
        if (isFatalEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setLevel(MonitoringLevel.FATAL);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个带异常的致命消息
     *
     * @param message 消息内容
     * @param t
     */
    @Override
    public void fatal(Object message, Throwable t) {
        if (isFatalEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setThrowable(t);
            carrier.setLevel(MonitoringLevel.FATAL);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个消息
     *
     * @param message 消息内容
     */
    @Override
    public void info(Object message) {
        if (isInfoEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setLevel(MonitoringLevel.INFO);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个带异常的消息
     *
     * @param message 消息内容
     * @param t       异常
     */
    @Override
    public void info(Object message, Throwable t) {
        if (isInfoEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setThrowable(t);
            carrier.setLevel(MonitoringLevel.INFO);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个跟踪等级的消息
     *
     * @param message 消息内容
     */
    @Override
    public void trace(Object message) {
        if (isTraceEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setLevel(MonitoringLevel.TRACE);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个带异常的跟踪等级的消息
     *
     * @param message 消息内容
     * @param t       异常
     */
    @Override
    public void trace(Object message, Throwable t) {
        if (isTraceEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setThrowable(t);
            carrier.setLevel(MonitoringLevel.TRACE);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个警告等级的消息
     *
     * @param message 消息内容
     */
    @Override
    public void warn(Object message) {
        if (isWarnEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setLevel(MonitoringLevel.WARN);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 发送一个带异常的警告等级的消息
     *
     * @param message 消息内容
     * @param t       异常
     */
    @Override
    public void warn(Object message, Throwable t) {
        if (isWarnEnabled) {
            MonitoringCarrier carrier = new MonitoringCarrier();
            carrier.setMessage(message);
            carrier.setClazz(fromClass);
            carrier.setThrowable(t);
            carrier.setLevel(MonitoringLevel.WARN);
            carrier.setDate(new Date());
            boolean success = blockingQueue.offer(new QueueEntry(carrier, this.sender));
            if (!success) {
                logger.error("消息发送过快容器无法装载");
            }
        }
    }

    /**
     * 是否发送debug等级消息
     *
     * @return
     */
    @Override
    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    /**
     * 是否发送错误等级消息
     *
     * @return
     */
    @Override
    public boolean isErrorEnabled() {
        return isErrorEnabled;
    }

    /**
     * 是否发送致命等级的消息
     *
     * @return
     */
    @Override
    public boolean isFatalEnabled() {
        return isFatalEnabled;
    }

    /**
     * 是否发送提示等级消息
     *
     * @return
     */
    @Override
    public boolean isInfoEnabled() {
        return isInfoEnabled;
    }

    /**
     * 是否发送跟踪等级的消息
     *
     * @return
     */
    @Override
    public boolean isTraceEnabled() {
        return isTraceEnabled;
    }

    /**
     * 是否发送警告等级的消息
     *
     * @return
     */
    @Override
    public boolean isWarnEnabled() {
        return isWarnEnabled;
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
        this.isErrorEnabled = is;
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setFatalEnabled(boolean is) {
        this.isFatalEnabled = is;
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setInfoEnabled(boolean is) {
        this.isInfoEnabled = is;
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setTraceEnabled(boolean is) {
        this.isTraceEnabled = is;
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setWarnEnabled(boolean is) {
        this.isWarnEnabled = is;
    }

    /**
     * 一下几个方法是一样的 只是设置某个等级的日志是否打印
     *
     * @param is 是否
     * @return
     */
    @Override
    public void setDebugEnabled(boolean is) {
        this.isDebugEnabled = is;
    }
}
