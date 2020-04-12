package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;

public class AddCompareMapping extends NothingCompareMapping {
    public AddCompareMapping(DataSourceWrapper dataSourceWrapper) {
        super(dataSourceWrapper);
    }

    @Override
    public void doMapping() throws SQLException {
        super.doMapping();
    }
}
