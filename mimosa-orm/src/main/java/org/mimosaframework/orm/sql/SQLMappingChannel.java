package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilderCombine;

public interface SQLMappingChannel extends UnifyBuilder {
    MappingTable getMappingTableByClass(Class table);

    void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper);

    SQLBuilderCombine getPlanSql();
}
