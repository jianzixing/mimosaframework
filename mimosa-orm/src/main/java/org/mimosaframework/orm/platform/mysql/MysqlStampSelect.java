package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampSelect;
import org.mimosaframework.orm.sql.stamp.StampSelectBuilder;

public class MysqlStampSelect implements StampSelectBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampSelect select) {
        return null;
    }
}
