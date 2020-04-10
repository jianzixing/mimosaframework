package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.Iterator;
import java.util.List;

public class MysqlStampStructure implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampStructure structure = (StampStructure) action;
        String schema = structure.schema;
        StringBuilder sb = new StringBuilder();

        if (structure.type == 0) {
            sb.append(
                    "SELECT " +
                            "TABLE_SCHEMA AS TABSCHEMA," +
                            "TABLE_NAME AS TABNAME," +
                            "TABLE_TYPE AS TYPE," +
                            "TABLE_ROWS AS COUNT," +
                            "UPDATE_TIME AS LASTUSED," +
                            "TABLE_COMMENT AS COMMENT," +
                            "CREATE_TIME " +
                            "FROM INFORMATION_SCHEMA.TABLES"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" WHERE TABLE_SCHEMA = '" + schema + "'");
            }
        }
        if (structure.type == 1) {
            sb.append(
                    "SELECT " +
                            "TABLE_SCHEMA             AS TABSCHEMA," +
                            "TABLE_NAME               AS TABNAME," +
                            "COLUMN_NAME              AS COLNAME," +
                            "DATA_TYPE                AS TYPENAME," +
                            "CHARACTER_MAXIMUM_LENGTH AS LENGTH," +
                            "NUMERIC_SCALE            AS SCALE," +
                            "COLUMN_DEFAULT           AS `DEFAULT`," +
                            "(CASE WHEN IS_NULLABLE = 'NO' THEN 'N' ELSE 'Y' END) AS IS_NULLABLE," +
                            "(CASE WHEN POSITION('auto_increment' IN EXTRA) > 0 THEN 'Y' ELSE 'N' END) AS AUTO_INCREMENT," +
                            "COLUMN_COMMENT                                       AS COMMENT " +
                            "FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_NAME IN (" + this.getTableNames(structure) + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND TABLE_SCHEMA = '" + schema + "'");
            }
        }
        if (structure.type == 2) {
            sb.append(
                    "SELECT " +
                            "TABLE_SCHEMA AS TABSCHEMA," +
                            "INDEX_NAME AS INDNAME," +
                            "TABLE_NAME AS TABNAME," +
                            "(CASE WHEN NON_UNIQUE=1 THEN 'U' WHEN INDEX_NAME='PRIMARY' THEN 'P' ELSE 'D' END) AS TYPE," +
                            "COLUMN_NAME AS COLNAME," +
                            "INDEX_COMMENT AS COMMENT " +
                            "FROM INFORMATION_SCHEMA.STATISTICS " +
                            "WHERE TABLE_NAME IN (" + this.getTableNames(structure) + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND TABLE_SCHEMA = '" + schema + "'");
            }
        }
        if (structure.type == 3) {
            sb.append(
                    "SELECT A.TABLE_SCHEMA AS TABSCHEMA," +
                            "A.CONSTRAINT_NAME AS CONSTNAME," +
                            "A.TABLE_NAME AS TABNAME," +
                            "B.COLUMN_NAME AS COLNAME," +
                            "B.REFERENCED_TABLE_NAME AS FGNTABNAME," +
                            "B.REFERENCED_COLUMN_NAME AS FGNCOLNAME," +
                            "(CASE" +
                            " WHEN A.CONSTRAINT_TYPE = 'PRIMARY KEY' THEN 'P'" +
                            " WHEN A.CONSTRAINT_TYPE = 'UNIQUE' THEN 'U'" +
                            " WHEN A.CONSTRAINT_TYPE = 'CHECK' THEN 'C'" +
                            " WHEN A.CONSTRAINT_TYPE = 'FOREIGN KEY' THEN 'F' END) AS TYPE " +
                            "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS A " +
                            "LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND A.TABLE_SCHEMA = B.TABLE_SCHEMA " +
                            "WHERE A.TABLE_NAME IN (" + this.getTableNames(structure) + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND A.TABLE_SCHEMA = '" + schema + "'");
            }
        }
        return new SQLBuilderCombine(sb.toString(), null);
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
