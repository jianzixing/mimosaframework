package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.sql.SQLException;

public class NothingCompareMapping implements StartCompareMapping {
    protected ActionDataSourceWrapper dataSourceWrapper;
    protected NotMatchObject notMatchObject;

    public NothingCompareMapping(ActionDataSourceWrapper dataSourceWrapper, NotMatchObject notMatchObject) {
        this.dataSourceWrapper = dataSourceWrapper;
        this.notMatchObject = notMatchObject;
    }

    @Override
    public void doMapping() throws SQLException {

    }
}
