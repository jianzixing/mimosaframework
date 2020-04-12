package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.List;

public class UpdateCompareMapping extends AddCompareMapping {

    public UpdateCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, DataSourceWrapper dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.UPDATE;
    }
}
