package org.mimosaframework.orm.sql.stamp;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;

public interface StampInsertBuilder {
    SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampInsert insert);
}
