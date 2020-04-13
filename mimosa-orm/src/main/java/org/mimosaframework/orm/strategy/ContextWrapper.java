package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.ContextContainer;
import org.mimosaframework.orm.platform.DataSourceWrapper;

public class ContextWrapper {
    private ContextContainer container;

    public ContextWrapper(ContextContainer container) {
        this.container = container;
    }

    public DataSourceWrapper getDefaultDataSourceWrapper(boolean isCreateNew) {
        return this.container.getDefaultDataSourceWrapper(isCreateNew);
    }
}
