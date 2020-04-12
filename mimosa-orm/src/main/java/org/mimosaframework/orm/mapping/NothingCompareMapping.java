package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.platform.DataSourceWrapper;
import org.mimosaframework.orm.platform.PlatformExecutor;

import java.sql.SQLException;

public class NothingCompareMapping implements StartCompareMapping {
    protected DataSourceWrapper dataSourceWrapper;
    protected PlatformExecutor executor = new PlatformExecutor();

    public NothingCompareMapping(DataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    @Override
    public void doMapping() throws SQLException {

    }
}
