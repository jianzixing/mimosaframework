package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.Iterator;
import java.util.List;

public class SQLServerStampStructure implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampStructure structure = (StampStructure) action;
        String schema = structure.schema;
        StringBuilder sb = new StringBuilder();

        if (structure.type == 0) {
            sb.append(
                    "SELECT NULL AS TABSCHEMA," +
                            "A.name AS TABNAME," +
                            "A.type AS TYPE," +
                            "0 AS COUNT," +
                            "A.modify_date AS LASTUSED," +
                            "A.create_date AS CREATE_TIME," +
                            "CAST(C.value AS VARCHAR) AS COMMENT " +
                            "FROM SYS.OBJECTS A LEFT JOIN SYS.EXTENDED_PROPERTIES C ON C.major_id=A.object_id AND C.minor_id=0 WHERE A.TYPE='U'"
            );
        }
        if (structure.type == 1) {
            sb.append(
                    "SELECT NULL AS TABSCHEMA," +
                            "B.name AS TABNAME," +
                            "A.name AS COLNAME," +
                            "C.name AS TYPENAME," +
                            "A.max_length AS LENGTH," +
                            "A.scale AS SCALE," +
                            "D.definition AS \"DEFAULT\"," +
                            "(CASE WHEN A.is_nullable = 1 THEN 'Y' ELSE 'N' END) AS IS_NULLABLE," +
                            "(CASE WHEN A.is_identity = 1 THEN 'Y' ELSE 'N' END) AS AUTO_INCREMENT," +
                            "CAST(E.value AS VARCHAR) AS COMMENT " +
                            "FROM SYS.COLUMNS A " +
                            "INNER JOIN SYS.OBJECTS B ON A.object_id = B.object_id " +
                            "LEFT JOIN SYS.TYPES C ON C.user_type_id = A.user_type_id " +
                            "LEFT JOIN SYS.DEFAULT_CONSTRAINTS D ON A.object_id = D.object_id " +
                            "LEFT JOIN SYS.EXTENDED_PROPERTIES E ON E.minor_id = A.column_id AND E.major_id = A.object_id AND E.class = 1 " +
                            "WHERE B.name IN (" + this.getTableNames(structure) + ")"
            );
        }
        if (structure.type == 2) {
            sb.append(
                    "SELECT NULL AS TABSCHEMA," +
                            "B.NAME AS INDNAME," +
                            "A.NAME AS TABNAME," +
                            "(CASE WHEN B.IS_PRIMARY_KEY=1 THEN 'P'" +
                            " WHEN B.IS_UNIQUE=1 THEN 'U'" +
                            " ELSE 'D' END) AS TYPE," +
                            "D.NAME AS COLNAME " +
                            "FROM SYS.OBJECTS A " +
                            "INNER JOIN SYS.INDEXES B ON A.OBJECT_ID = B.OBJECT_ID AND B.NAME IS NOT NULL " +
                            "LEFT JOIN SYS.INDEX_COLUMNS C ON C.OBJECT_ID = A.OBJECT_ID " +
                            "LEFT JOIN SYS.COLUMNS D ON D.OBJECT_ID = A.OBJECT_ID AND C.COLUMN_ID = D.COLUMN_ID " +
                            "WHERE A.TYPE = 'U' AND A.NAME IN (" + this.getTableNames(structure) + ")"
            );
        }
        if (structure.type == 3) {
            sb.append(
                    "SELECT A.CONSTRAINT_NAME AS CONSNAME," +
                            "A.TABLE_NAME AS TABNAME," +
                            "B.COLUMN_NAME AS COLNAME," +
                            "C.TABLE_NAME AS FGNTABNAME," +
                            "C.COLUMN_NAME AS FGNCOLNAME," +
                            "(CASE" +
                            " WHEN A.CONSTRAINT_TYPE = 'PRIMARY KEY' THEN 'P'" +
                            " WHEN A.CONSTRAINT_TYPE = 'UNIQUE' THEN 'U'" +
                            " WHEN A.CONSTRAINT_TYPE = 'CHECK' THEN 'C'" +
                            " WHEN A.CONSTRAINT_TYPE = 'FOREIGN KEY' THEN 'F' END) AS TYPE " +
                            "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS A " +
                            "LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME " +
                            "LEFT JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE C ON A.CONSTRAINT_NAME = C.CONSTRAINT_NAME " +
                            "WHERE A.TABLE_NAME IN (" + this.getTableNames(structure) + ")"
            );
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
