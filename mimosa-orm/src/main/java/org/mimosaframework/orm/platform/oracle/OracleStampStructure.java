package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.Iterator;
import java.util.List;

/**
 * SELECT
 * NULL       AS TABSCHEMA,
 * T1.TABLE_NAME AS TABNAME,
 * T2.TABLE_TYPE AS TYPE,
 * T1.NUM_ROWS AS COUNT,
 * NULL       AS LASTUSED,
 * NULL       AS CREATE_TIME,
 * T2.COMMENTS   AS "COMMENT"
 * FROM USER_TABLES T1
 * LEFT JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME=T2.TABLE_NAME
 * WHERE T1.TABLE_NAME='T_USER';
 * <p>
 * SELECT
 * NULL AS TABSCHEMA,
 * T1.TABLE_NAME AS TABNAME,
 * T1.COLUMN_NAME AS COLNAME,
 * T1.DATA_TYPE AS TYPENAME,
 * T1.DATA_LENGTH AS LENGTH,
 * T1.DATA_SCALE AS SCALE,
 * T1.DATA_DEFAULT AS "DEFAULT",
 * T1.NULLABLE AS IS_NULLABLE,
 * 'N' AS AUTO_INCREMENT,
 * (CASE WHEN T1.COLUMN_NAME=T3.COLUMN_NAME THEN 'Y' ELSE 'N' END) AS PK,
 * T4.COMMENTS AS "COMMENT"
 * FROM USER_TAB_COLS T1
 * LEFT JOIN USER_CONSTRAINTS T2 ON T1.TABLE_NAME=T2.TABLE_NAME AND T2.CONSTRAINT_TYPE='P'
 * LEFT JOIN USER_CONS_COLUMNS T3 ON T2.CONSTRAINT_NAME=T3.CONSTRAINT_NAME AND T1.TABLE_NAME=T3.TABLE_NAME
 * LEFT JOIN USER_COL_COMMENTS T4 ON T4.TABLE_NAME=T1.TABLE_NAME AND T4.COLUMN_NAME=T1.COLUMN_NAME
 * WHERE T1.TABLE_NAME ='T_USER';
 * <p>
 * <p>
 * SELECT
 * NULL AS TABSCHEMA,
 * T1.INDEX_NAME AS INDNAME,
 * T1.TABLE_NAME AS TABNAME,
 * (CASE WHEN T1.UNIQUENESS='UNIQUE' THEN 'U' ELSE 'D' END) AS TYPE,
 * T2.COLUMN_NAME AS COLNAME
 * FROM USER_INDEXES T1
 * LEFT JOIN USER_IND_COLUMNS T2 ON T1.TABLE_NAME=T2.TABLE_NAME AND T1.INDEX_NAME=T2.INDEX_NAME;
 */
public class OracleStampStructure implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampStructure structure = (StampStructure) action;
        String schema = structure.schema;
        StringBuilder sb = new StringBuilder();

        if (structure.type == 0) {
            sb.append(
                    "SELECT NULL AS TABSCHEMA," +
                            "T1.TABLE_NAME AS TABNAME," +
                            "T2.TABLE_TYPE AS TYPE," +
                            "T1.NUM_ROWS AS COUNT," +
                            "NULL AS LASTUSED," +
                            "NULL AS CREATE_TIME," +
                            "T2.COMMENTS AS \"COMMENT\" " +
                            "FROM USER_TABLES T1 " +
                            "LEFT JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME=T2.TABLE_NAME"
            );
        }
        if (structure.type == 1) {
            sb.append(
                    "SELECT " +
                            "NULL AS TABSCHEMA," +
                            "T1.TABLE_NAME AS TABNAME," +
                            "T1.COLUMN_NAME AS COLNAME," +
                            "T1.DATA_TYPE AS TYPENAME," +
                            "T1.DATA_LENGTH AS LENGTH," +
                            "T1.DATA_SCALE AS SCALE," +
                            "T1.DATA_DEFAULT AS \"DEFAULT\"," +
                            "T1.NULLABLE AS IS_NULLABLE," +
                            "'N' AS AUTO_INCREMENT," +
                            "(CASE WHEN T1.COLUMN_NAME=T3.COLUMN_NAME THEN 'Y' ELSE 'N' END) AS PK," +
                            " T4.COMMENTS AS \"COMMENT\" " +
                            "FROM USER_TAB_COLS T1 " +
                            "LEFT JOIN USER_CONSTRAINTS T2 ON T1.TABLE_NAME=T2.TABLE_NAME AND T2.CONSTRAINT_TYPE='P' " +
                            "LEFT JOIN USER_CONS_COLUMNS T3 ON T2.CONSTRAINT_NAME=T3.CONSTRAINT_NAME AND T1.TABLE_NAME=T3.TABLE_NAME " +
                            "LEFT JOIN USER_COL_COMMENTS T4 ON T4.TABLE_NAME=T1.TABLE_NAME AND T4.COLUMN_NAME=T1.COLUMN_NAME " +
                            "WHERE T1.TABLE_NAME IN (" + this.getTableNames(structure) + ")"
            );
        }
        if (structure.type == 2) {
            sb.append(
                    "SELECT " +
                            "NULL AS TABSCHEMA," +
                            "T1.INDEX_NAME AS INDNAME," +
                            "T1.TABLE_NAME AS TABNAME," +
                            "(CASE WHEN T1.UNIQUENESS='UNIQUE' THEN 'U' ELSE 'D' END) AS TYPE," +
                            "T2.COLUMN_NAME AS COLNAME " +
                            "FROM USER_INDEXES T1 " +
                            "LEFT JOIN USER_IND_COLUMNS T2 ON T1.TABLE_NAME=T2.TABLE_NAME AND T1.INDEX_NAME=T2.INDEX_NAME " +
                            "WHERE T1.TABLE_NAME IN (" + this.getTableNames(structure) + ")"
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
