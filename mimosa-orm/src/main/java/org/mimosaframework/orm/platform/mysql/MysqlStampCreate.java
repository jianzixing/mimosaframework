package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampCreate;
import org.mimosaframework.orm.sql.stamp.StampCreateBuilder;

public class MysqlStampCreate implements StampCreateBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampCreate create) {
        return null;
    }
}
