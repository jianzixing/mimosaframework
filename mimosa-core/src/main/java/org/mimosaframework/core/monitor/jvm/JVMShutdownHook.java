package org.mimosaframework.core.monitor.jvm;

public interface JVMShutdownHook {
    String getName();

    void shutdown();
}
