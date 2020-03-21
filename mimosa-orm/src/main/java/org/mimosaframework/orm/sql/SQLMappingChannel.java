package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilderCombine;

public interface SQLMappingChannel {
    MappingTable getMappingTableByClass(Class table);

    SQLBuilderCombine getPlanSql();
}
