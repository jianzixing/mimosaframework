package org.mimosaframework.orm.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.SessionContext;

public class WarnCompareMapping extends NothingCompareMapping {
    private static final Log logger = LogFactory.getLog(WarnCompareMapping.class);

    public WarnCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.WARN;
    }
}
