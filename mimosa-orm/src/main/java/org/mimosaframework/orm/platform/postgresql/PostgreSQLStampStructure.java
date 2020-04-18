package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.Iterator;
import java.util.List;

public class PostgreSQLStampStructure implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampStructure structure = (StampStructure) action;
        String schema = structure.schema;
        StringBuilder sb = new StringBuilder();

        if (structure.type == 0) {
            sb.append(
                    "SELECT T2.SCHEMANAME AS \"TABSCHEMA\"," +
                            "T2.TABLENAME AS \"TABNAME\"," +
                            "NULL AS \"TYPE\"," +
                            "T1.RELTUPLES AS \"COUNT\"," +
                            "NULL AS \"LASTUSED\"," +
                            "NULL AS \"CREATE_TIME\"," +
                            "CAST(OBJ_DESCRIPTION(T1.RELFILENODE,'pg_class') AS VARCHAR) AS \"COMMENT\" " +
                            "FROM PG_CLASS T1,(SELECT * FROM PG_TABLES WHERE SCHEMANAME='public') T2 " +
                            "WHERE T1.RELNAME=T2.TABLENAME"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND T2.schemaname='" + schema + "'");
            }
        }
        if (structure.type == 1) {
            sb.append(
                    "SELECT " +
                            "T1.table_schema AS \"TABSCHEMA\"," +
                            "T1.table_name AS \"TABNAME\"," +
                            "T1.column_name AS \"COLNAME\"," +
                            "T1.udt_name AS \"TYPENAME\"," +
                            "(CASE WHEN T1.numeric_precision>0 THEN T1.numeric_precision ELSE T1.character_maximum_length END) AS \"LENGTH\"," +
                            "T1.numeric_scale AS \"SCALE\"," +
                            "T1.column_default AS \"DEFAULT\"," +
                            "(CASE WHEN T1.is_nullable='YES' THEN 'Y' ELSE 'N' END) AS \"IS_NULLABLE\"," +
                            "(CASE WHEN T1.is_identity = 'YES' THEN 'Y' WHEN POSITION('nextval' in T1.column_default)=1 THEN 'Y' ELSE 'N' END) AS \"AUTO_INCREMENT\"," +
                            "T2.description AS \"COMMENT\" " +
                            "FROM information_schema.columns T1 " +
                            "LEFT JOIN pg_description T2 ON T1.table_name::regclass=T2.objoid AND T1.ordinal_position = T2.objsubid " +
                            "where T1.table_name IN (" + this.getTableNames(structure) + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND T1.table_schema='" + schema + "'");
            }
        }
        if (structure.type == 2) {
            sb.append(
                    "select T5.SCHEMANAME AS \"TABSCHEMA\"," +
                            "T2.relname AS \"INDNAME\"," +
                            "T1.relname AS \"TABNAME\"," +
                            "(CASE WHEN T3.INDISUNIQUE THEN 'U' ELSE 'D' END) AS \"TYPE\"," +
                            "T4.attname AS \"COLNAME\"," +
                            "NULL AS \"COMMENT\" " +
                            "FROM pg_class T1," +
                            "pg_class T2," +
                            "pg_index T3," +
                            "pg_attribute T4," +
                            "(SELECT * FROM PG_TABLES WHERE SCHEMANAME = 'public') T5 " +
                            "WHERE T1.oid = T3.indrelid " +
                            " AND T2.oid = T3.indexrelid" +
                            " AND T4.attrelid = T1.oid" +
                            " AND T4.attnum = ANY (T3.indkey)" +
                            " AND T1.relkind = 'r'" +
                            " AND T1.relname IN (" + this.getTableNames(structure) + ")" +
                            " AND T5.tablename = T1.relname"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND T5.schemaname='" + schema + "'");
            }
        }

        if (structure.type == 3) {
            sb.append(
                    "SELECT tc.constraint_name AS \"CONSNAME\"," +
                            "tc.table_name AS \"TABNAME\"," +
                            "kcu.column_name AS \"COLNAME\"," +
                            "ccu.table_name AS \"FGNTABNAME\"," +
                            "ccu.column_name AS \"FGNCOLNAME\"," +
                            "(CASE " +
                            "WHEN tc.constraint_type = 'PRIMARY KEY' THEN 'P' " +
                            "WHEN tc.constraint_type = 'UNIQUE' THEN 'U' " +
                            "WHEN tc.constraint_type = 'CHECK' THEN 'C' " +
                            "WHEN tc.constraint_type = 'FOREIGN KEY' THEN 'F' END) AS \"TYPE\" " +
                            "FROM information_schema.table_constraints AS tc " +
                            "LEFT JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name " +
                            "LEFT JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name " +
                            "WHERE tc.table_name IN (" + this.getTableNames(structure) + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append("AND tc.table_schema='" + schema + "'");
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
