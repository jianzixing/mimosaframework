package org.mimosaframework.orm;

/**
 * 短暂的上下文配置
 * 只在某次单个的Session接口操作时有效
 * Session接口的方法执行一次后立即销毁
 */
public class SessionBrevityBuilder {
    private static ThreadLocal<SessionBrevityBuilder> context = null;

    private static final void init() {
        if (context != null) {
            SessionBrevityBuilder.context = new ThreadLocal<>();
        }
    }

    public static final SessionBrevityBuilder build() {
        init();
        if (SessionBrevityBuilder.context != null) {
            SessionBrevityBuilder brevityBuilder = new SessionBrevityBuilder();
            context.set(brevityBuilder);
            return SessionBrevityBuilder.context.get();
        }
        return null;
    }

    public static final void release() {
        if (SessionBrevityBuilder.context != null) {
            SessionBrevityBuilder.context.remove();
        }
    }

    /**
     * 暂时不实现了,Session层做缓存、搜索的意义不大
     * 业务上和建表设计上不同导致缓存和搜索方法也不一样
     * 所以还是交给业务层做
     *
     * @return
     */
    public static final SessionBrevityBuilder getBuildBrevity() {
//        if (SessionBrevityBuilder.context != null) {
//            return SessionBrevityBuilder.context.get();
//        }
        return null;
    }

    /**
     * 忽略非数据库报错,这样在事务中不会影响事务本身
     */
    private boolean skipException = false;

    public boolean isSkipException() {
        return skipException;
    }

    public void setSkipException(boolean skipException) {
        this.skipException = skipException;
    }
}
