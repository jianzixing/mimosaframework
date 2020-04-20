package org.mimosaframework.orm.platform.postgresql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class PostgreSQLStampAlter extends PostgreSQLStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(PostgreSQLStampAlter.class);

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper,
                                           StampAction action) {
        StampAlter alter = (StampAlter) action;
        StringBuilder sb = new StringBuilder();
        if (alter.target == KeyTarget.DATABASE) {

            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append("UPDATE PG_DATABASE SET ENCODING = PG_CHAR_TO_ENCODING('" + alter.charset + "') " +
                        "WHERE DATNAME = '" + alter.databaseName + "'");
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
        }
        return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        sb.append("ALTER");
        sb.append(" TABLE");
        String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);
        sb.append(" " + tableName);
        if (item.action == KeyAction.ADD) {
            sb.append(" ADD");
            if (item.struct == KeyAlterStruct.COLUMN) {
                this.buildAddAlterColumn(sb, wrapper, alter, item);
            }
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.MODIFY) {
            this.buildAlterColumn(sb, wrapper, alter, item, 3);
        }

        if (item.action == KeyAction.DROP) {
            sb.append(" DROP");
            if (item.dropType == KeyAlterDropType.COLUMN) {
                sb.append(" COLUMN");
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.setLength(0);
                String outTableName = this.getTableName(wrapper, alter.tableClass, alter.tableName, false);
                this.getDeclares().add("PK_INDEX_NAME VARCHAR(5000)");
                this.getBegins().add(new ExecuteImmediate()
                        .setProcedure("PK_INDEX_NAME = (SELECT CONNAME FROM PG_CONSTRAINT A, PG_CLASS B " +
                                "WHERE A.CONRELID = B.OID " +
                                "AND B.RELNAME = '" + outTableName + "' " +
                                "AND A.CONTYPE = 'p' LIMIT 1)"));
                sb.append("EXECUTE 'ALTER TABLE " + tableName + " DROP CONSTRAINT '||PK_INDEX_NAME");
            }
        }

        if (item.action == KeyAction.AUTO_INCREMENT) {
            sb.setLength(0);
            String outTableName = this.getTableName(wrapper, alter.tableClass, alter.tableName, false);
            this.getDeclares().add("AUTO_INCRE_SEQ_NAME VARCHAR(5000)");
            this.getBegins().add(new ExecuteImmediate().setProcedure(
                    "AUTO_INCRE_SEQ_NAME = (SELECT ARRAY_TO_STRING(REGEXP_MATCHES(COLUMN_DEFAULT, '[\"|''](.*)[\"|'']','gi'),'') " +
                            "FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_NAME = '" + outTableName + "' " +
                            "AND POSITION('nextval' IN COLUMN_DEFAULT) > 0 LIMIT 1)"
            ));
            sb.append("EXECUTE 'ALTER SEQUENCE '||AUTO_INCRE_SEQ_NAME||' RESTART WITH " + item.value + "'");
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            sb.setLength(0);
            logger.warn("postgresql can't set table charset");
        }
        if (item.action == KeyAction.COMMENT) {
            this.addCommentSQL(wrapper, alter, null, item.comment, 2);
        }
    }

    private void buildAlterIndex(StringBuilder sb,
                                 MappingGlobalWrapper wrapper,
                                 StampAlter alter,
                                 StampAlterItem item) {
        String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);
        String outTableName = this.getTableName(wrapper, alter.tableClass, alter.tableName, false);
        sb.setLength(0);
        if (item.indexType != KeyIndexType.PRIMARY_KEY) {
            sb.append("CREATE");
        } else {
            sb.append("ALTER TABLE " + tableName + " ADD CONSTRAINT ");
        }
        if (item.indexType == KeyIndexType.UNIQUE) {
            sb.append(" UNIQUE");
            sb.append(" INDEX");
        } else if (item.indexType == KeyIndexType.PRIMARY_KEY) {

        } else {
            sb.append(" INDEX");
        }

        if (StringTools.isNotEmpty(item.indexName)) {
            sb.append(" " + item.indexName);
        } else {
            sb.append(" " + outTableName + "_pkey");
        }
        if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            sb.append(" PRIMARY KEY");
        } else {
            sb.append(" ON ");
            sb.append(tableName);
        }

        if (item.columns != null) {
            sb.append(" (");
            int i = 0;
            for (StampColumn column : item.columns) {
                sb.append(this.getColumnName(wrapper, alter, column));
                i++;
                if (i != item.columns.length) sb.append(",");
            }
            sb.append(")");
        } else {
            throw new IllegalArgumentException(I18n.print("miss_index_columns"));
        }

        if (StringTools.isNotEmpty(item.comment)) {
            logger.warn("postgresql can't set index comment");
        }
    }

    private void buildAddAlterColumn(StringBuilder sb,
                                     MappingGlobalWrapper wrapper,
                                     StampAlter alter,
                                     StampAlterItem column) {
        sb.append(" " + this.getColumnName(wrapper, alter, column.column));
        if (column.columnType != null) {
            sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
        }
        if (column.autoIncrement == KeyConfirm.YES) {
            if (column.columnType.equals(KeyColumnType.INT)) {
                sb.append(" SERIAL");
            } else if (column.columnType.equals(KeyColumnType.SMALLINT)
                    || column.columnType.equals(KeyColumnType.TINYINT)) {
                sb.append(" SMALLSERIAL");
            } else {
                sb.append(" BIGSERIAL");
            }
        }
        if (column.nullable == KeyConfirm.NO) {
            sb.append(" NOT NULL");
        }
        if (column.pk == KeyConfirm.YES) {
            sb.append(" PRIMARY KEY");
        }
        if (column.defaultValue != null) {
            if (column.defaultValue.equals("*****")) {
                sb.append(" DEFAULT NULL");
            } else {
                sb.append(" DEFAULT '" + column.defaultValue + "'");
            }
        }
        if (StringTools.isNotEmpty(column.comment)) {
            this.addCommentSQL(wrapper, alter, column.column, column.comment, 1);
        }
        if (column.after != null) {
            logger.warn("postgresql can't set column order");
        }
        if (column.before != null) {
            logger.warn("postgresql can't set column order");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column,
                                  int type) {
        sb.append(" ALTER COLUMN");
        sb.append(" " + this.getColumnName(wrapper, alter, type == 2 ? column.oldColumn : column.column));
        if (column.columnType != null) {
            sb.append(" TYPE");
            sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
        }
        if (column.autoIncrement == KeyConfirm.YES) {
            if (type == 3) {
                String outTableName = this.getTableName(wrapper, alter.tableClass, alter.tableName, false);
                String outColumnName = this.getColumnName(wrapper, alter, column.column, false);
                this.getDeclares().add("IS_AUTOINCRE INT");
                this.getDeclares().add("HAS_SEQUENCE_AI INT");
                String oldSeq = outTableName + "_" + outColumnName;
                this.getBuilders().add(new ExecuteImmediate().setProcedure(
                        "HAS_SEQUENCE_AI = (SELECT COUNT(1) FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME='"
                                + oldSeq + "')"
                ));
                this.getBuilders().add(new ExecuteImmediate().setProcedure(
                        "IS_AUTOINCRE = (SELECT COUNT(1) AS AUTO_INCRE_COUNT " +
                                "FROM INFORMATION_SCHEMA.COLUMNS " +
                                "WHERE TABLE_NAME='" + outTableName + "' " +
                                "AND COLUMN_NAME='" + outColumnName + "' " +
                                "AND POSITION('nextval' IN COLUMN_DEFAULT) > 0)"
                ));
                this.getBuilders().add(new ExecuteImmediate().setProcedure("" +
                        "IF IS_AUTOINCRE = 0 THEN " +
                        NL_TAB + "IF HAS_SEQUENCE_AI = 0 THEN " +
                        NL_TAB + "CREATE SEQUENCE " + oldSeq + " START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1; " +
                        NL_TAB + "END IF; " +
                        NL_TAB + "ALTER TABLE EVENT ALTER COLUMN ID SET DEFAULT NEXTVAL('" + oldSeq + "');" +
                        "END IF"
                ));
            } else {
                if (column.columnType.equals("INT")) {
                    sb.append(" SERIAL");
                } else if (column.columnType.equals("SMALLINT")) {
                    sb.append(" SMALLSERIAL");
                } else {
                    sb.append(" BIGSERIAL");
                }
            }
        }
        if (column.nullable == KeyConfirm.NO) {
            sb.append(" NOT NULL");
        }
        if (column.pk == KeyConfirm.YES) {
            sb.append(" PRIMARY KEY");
        }
        if (column.defaultValue != null) {
            if (column.defaultValue.equals("*****")) {
                sb.append(" SET DEFAULT NULL");
            } else {
                sb.append(" SET DEFAULT '" + column.defaultValue + "'");
            }
        }
        if (StringTools.isNotEmpty(column.comment)) {
            this.addCommentSQL(wrapper, alter, type == 2 ? column.oldColumn : column.column, column.comment, 1);
        }
        if (column.after != null) {
            logger.warn("postgresql can't set column order");
        }
        if (column.before != null) {
            logger.warn("postgresql can't set column order");
        }
    }
}
