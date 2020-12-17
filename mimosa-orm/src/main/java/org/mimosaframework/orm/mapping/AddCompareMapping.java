package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.SessionContext;

public class AddCompareMapping extends NothingCompareMapping {
    public AddCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.CREATE;
    }
}
