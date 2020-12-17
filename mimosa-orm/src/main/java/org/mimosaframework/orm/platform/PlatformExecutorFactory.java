package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;

public class PlatformExecutorFactory {
    public static PlatformExecutor getExecutor(MappingGlobalWrapper mappingGlobalWrapper,
                                               SessionContext sessionContext) {
        PlatformExecutor executor = new PlatformExecutor(mappingGlobalWrapper, sessionContext);
        return executor;
    }
}
