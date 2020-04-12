package org.mimosaframework.orm.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.platform.DataSourceWrapper;

import java.sql.SQLException;

public class WarnCompareMapping extends NothingCompareMapping {
    private static final Log logger = LogFactory.getLog(WarnCompareMapping.class);

    public WarnCompareMapping(DataSourceWrapper dataSourceWrapper) {
        super(dataSourceWrapper);
    }

    @Override
    public void doMapping() throws SQLException {
        
    }
}
