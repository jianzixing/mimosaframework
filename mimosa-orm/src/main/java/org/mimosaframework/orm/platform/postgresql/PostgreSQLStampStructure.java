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
                    "SELECT T1.schemaname AS \"TABSCHEMA\"," +
                            "T1.tablename AS \"TABNAME\"," +
                            "NULL AS \"TYPE\"," +
                            "T2.reltuples AS \"COUNT\"," +
                            "NULL AS \"LASTUSED\"," +
                            "NULL AS \"CREATE_TIME\"," +
                            "T3.description AS \"COMMENT\" " +
                            "FROM PG_TABLES T1 " +
                            "LEFT JOIN PG_CLASS T2 ON T1.tablename = T2.relname " +
                            "LEFT JOIN PG_DESCRIPTION T3 ON T2.oid = T3.objoid"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" WHERE T1.schemaname='" + schema + "'");
            }
        }
        if (structure.type == 1) {
            sb.append(
                    "SELECT " +
                            "T1.table_schema AS \"TABSCHEMA\"," +
                            "T1.table_name AS \"TABNAME\"," +
                            "T1.column_name AS \"COLNAME\"," +
                            "T1.udt_name AS \"TYPENAME\"," +
                            "T1.character_maximum_length AS \"LENGTH\"," +
                            "T1.numeric_scale AS \"SCALE\"," +
                            "T1.column_default AS \"DEFAULT\"," +
                            "(CASE WHEN T1.is_nullable='YES' THEN 'Y' ELSE 'N' END) AS \"IS_NULLABLE\"," +
                            "(CASE WHEN T1.is_identity='NO' THEN 'N' ELSE 'Y' END) AS \"AUTO_INCREMENT\"," +
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
                    "SELECT A.SCHEMANAME AS \"TABSCHEMA\"," +
                            "A.INDEXNAME AS \"INDNAME\"," +
                            "A.TABLENAME AS \"TABNAME\"," +
                            "(CASE WHEN C.INDISUNIQUE THEN 'U' ELSE 'D' END) AS \"TYPE\"," +
                            "H.attname AS \"COLNAME\"," +
                            "D.DESCRIPTION AS \"COMMENT\" " +
                            "FROM pg_am B " +
                            "LEFT JOIN pg_class F ON B.OID = F.RELAM " +
                            "LEFT JOIN pg_index G ON F.OID=G.indexrelid " +
                            "LEFT JOIN pg_attribute H ON F.OID=H.attrelid AND H.attnum = ANY(G.indkey) " +
                            "LEFT JOIN pg_stat_all_indexes E ON F.OID = E.INDEXRELID " +
                            "LEFT JOIN pg_index C ON E.INDEXRELID = C.INDEXRELID " +
                            "LEFT OUTER JOIN pg_description D ON C.INDEXRELID = D.OBJOID," +
                            "pg_indexes A " +
                            "WHERE A.SCHEMANAME = E.SCHEMANAME " +
                            "AND A.TABLENAME = E.RELNAME " +
                            "AND A.INDEXNAME = E.INDEXRELNAME " +
                            "AND A.tablename IN (" + this.getTableNames(structure) + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND A.schemaname='" + schema + "'");
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
