package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;

public class PlatformExecutorFactory {
    public static PlatformExecutor getExecutor(MappingGlobalWrapper mappingGlobalWrapper,
                                               DataSourceWrapper dswrapper) {
        PlatformExecutor executor = new PlatformExecutor(mappingGlobalWrapper, dswrapper);
        return executor;
    }
}
