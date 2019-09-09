package org.mimosaframework.core.monitor.jvm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public final class JVMShutdownMonitor {
    private static final Log logger = LogFactory.getLog(JVMShutdownMonitor.class);
    private static final List<JVMShutdownHook> hooks = new ArrayList<JVMShutdownHook>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (hooks) {
                    for (JVMShutdownHook hook : hooks) {
                        try {
                            hook.shutdown();
                            if (hook.getName() != null) {
                                logger.info("注册的钩子回调 " + hook.getName() + " 已经关闭");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }));
        logger.info("添加JVM关闭钩子");
    }

    public static final void addShutdownHook(JVMShutdownHook hook) {
        synchronized (hooks) {
            hooks.add(hook);
        }
    }

    public static final void removeShutdownHook(JVMShutdownHook hook) {
        synchronized (hooks) {
            hooks.remove(hook);
        }
    }

}
