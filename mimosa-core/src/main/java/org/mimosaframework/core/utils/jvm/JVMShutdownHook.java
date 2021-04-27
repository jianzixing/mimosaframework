package org.mimosaframework.core.utils.jvm;

public interface JVMShutdownHook {
    String getName();

    void shutdown();
}
