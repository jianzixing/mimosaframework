package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampDelete;
import org.mimosaframework.orm.sql.stamp.StampDeleteBuilder;

public class MysqlStampDelete implements StampDeleteBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampDelete delete) {
        return null;
    }
}
