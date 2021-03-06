package org.mimosaframework.orm.sql.stamp;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;

public interface StampCombineBuilder {
    SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action);
}
