package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.DataSourceWrapper;

public class AddCompareMapping extends NothingCompareMapping {
    public AddCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, DataSourceWrapper dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.CREATE;
    }
}
