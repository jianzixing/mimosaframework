package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.mapping.MappingTable;

public interface SQLMappingChannel {
    MappingTable getMappingTableByClass(Class table);
}
