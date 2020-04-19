package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.Iterator;
import java.util.List;

public class SqliteStampStructure implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampStructure structure = (StampStructure) action;
        String schema = structure.schema;
        StringBuilder sb = new StringBuilder();

        if (structure.type == 0) {
            sb.append(
                    "SELECT NULL AS TABSCHEMA," +
                            "name AS TABNAME," +
                            "type AS TYPE," +
                            "0 AS COUNT," +
                            "NULL AS LASTUSED," +
                            "NULL AS CREATE_TIME," +
                            "NULL AS COMMENT," +
                            "sql AS SQL " +
                            "FROM SQLITE_MASTER WHERE TYPE='table'"
            );
        }
        if (structure.type == 1) {
            sb.append("pragma table_info (" + this.getTableName(structure) + ")");
        }
        if (structure.type == 2) {
            sb.append("select * from sqlite_master where type='index' " +
                    "and tbl_name in (" + this.getTableNames(structure) + ")");
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }

    private String getTableName(StampStructure structure) {
        List<String> tables = structure.tables;
        return "\"" + tables.get(0) + "\"";
    }

    private StringBuilder getTableNames(StampStructure structure) {
        StringBuilder tableNames = new StringBuilder();
        List<String> tables = structure.tables;
        Iterator<String> iterator = tables.iterator();
        while (iterator.hasNext()) {
            tableNames.append("'" + iterator.next() + "'");
            if (iterator.hasNext()) tableNames.append(",");
        }
        return tableNames;
    }
}
