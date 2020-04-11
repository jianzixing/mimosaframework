package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.ContextContainer;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.DataSourceWrapper;

public class ContextWrapper {
    private ContextContainer container;

    public ContextWrapper(ContextContainer container) {
        this.container = container;
    }

    public DataSourceWrapper getDefaultDataSourceWrapper(boolean isCreateNew) {
        return this.container.getDefaultDataSourceWrapper(isCreateNew);
    }

    public MimosaDataSource getDataSourceByName(String dataSourceName) {
        return container.getDataSourceByName(dataSourceName);
    }

    public boolean isExistTable(MimosaDataSource dataSource, String name) {
        MappingGlobalWrapper mappingGlobalWrapper = container.getMappingGlobalWrapper();
        MappingTable dbExistTable = mappingGlobalWrapper.getDatabaseTable(dataSource, name);
        if (dbExistTable == null) {
            return false;
        }
        return true;
    }
}
