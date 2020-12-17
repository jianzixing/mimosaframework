package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.SessionContext;

public class UpdateCompareMapping extends AddCompareMapping {

    public UpdateCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.UPDATE;
    }
}
