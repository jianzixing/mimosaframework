package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampDrop;
import org.mimosaframework.orm.sql.stamp.StampDropBuilder;

public class MysqlStampDrop extends MysqlAbstractStamp implements StampDropBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampDrop drop) {
        return null;
    }
}
