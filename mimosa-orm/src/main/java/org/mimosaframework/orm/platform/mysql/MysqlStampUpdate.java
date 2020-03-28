package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampUpdate;
import org.mimosaframework.orm.sql.stamp.StampUpdateBuilder;

public class MysqlStampUpdate implements StampUpdateBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampUpdate update) {
        return null;
    }
}
