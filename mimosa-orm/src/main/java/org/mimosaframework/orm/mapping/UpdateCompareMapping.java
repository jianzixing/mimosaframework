package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.List;

public class UpdateCompareMapping extends AddCompareMapping {

    public UpdateCompareMapping(DataSourceWrapper dataSourceWrapper) {
        super(dataSourceWrapper);
    }

    @Override
    public void doMapping() throws SQLException {
        super.doMapping();

        
    }
}
