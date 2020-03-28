package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampInsert;
import org.mimosaframework.orm.sql.stamp.StampInsertBuilder;

public class MysqlStampInsert extends MysqlAbstractStamp implements StampInsertBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampInsert insert) {
        return null;
    }
}
