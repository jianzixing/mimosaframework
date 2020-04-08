package org.mimosaframework.orm.platform.postgresql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
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
            sb.append("ALTER");
            sb.append(" DATABASE");

            sb.append(" " + RS + alter.name + RE);

            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append(" CHARSET " + alter.charset);
            }
            if (StringTools.isNotEmpty(alter.collate)) {
                sb.append(" COLLATE " + alter.collate);
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            sb.append("ALTER");
            sb.append(" TABLE");

            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append(" CHARSET " + alter.charset);
            }
            if (StringTools.isNotEmpty(alter.collate)) {
                sb.append(" COLLATE " + alter.collate);
            }
        }
        return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {

        String tableName = this.getTableName(wrapper, alter.table, alter.name);
        sb.append(" " + tableName);
        if (item.action == KeyAction.ADD) {
            sb.append(" ADD");
            if (item.struct == KeyAlterStruct.COLUMN) {
                this.buildAlterColumn(sb, wrapper, alter, item, 1);
            }
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.CHANGE) {
            String columnName = this.getColumnName(wrapper, alter, item.column);
            String oldColumnName = this.getColumnName(wrapper, alter, item.oldColumn);
            this.buildAlterColumn(sb, wrapper, alter, item, 2);
            if (!columnName.equalsIgnoreCase(oldColumnName)) {
                this.getBuilders().add(new ExecuteImmediate().setProcedure(
                        "ALTER TABLE " + tableName + " RENAME " +
                                oldColumnName + " TO " + columnName
                ));
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
            if (item.dropType == KeyAlterDropType.INDEX) {
                sb.append(" INDEX");
                sb.append(" " + item.name);
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.append(" PRIMARY KEY");
            }
        }

        if (item.action == KeyAction.RENAME) {
            sb.append(" RENAME");
            if (item.renameType == KeyAlterRenameType.COLUMN) {
                sb.append(" COLUMN");
                sb.append(" " + this.getColumnName(wrapper, alter, item.oldColumn));
                sb.append(" TO");
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.renameType == KeyAlterRenameType.INDEX) {
                sb.append(" INDEX");
                sb.append(" " + item.oldName);
                sb.append(" TO");
                sb.append(" " + item.name);
            }
            if (item.renameType == KeyAlterRenameType.TABLE) {
                sb.append(" " + item.name);
            }
        }

        if (item.action == KeyAction.AUTO_INCREMENT) {
            sb.append(" AUTO_INCREMENT = " + item.value);
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            sb.append(" CHARACTER SET = " + item.name);
        }
        if (item.action == KeyAction.COMMENT) {
            this.addCommentSQL(wrapper, alter, null, item.comment, 2);
        }
    }

    private void buildAlterIndex(StringBuilder sb,
                                 MappingGlobalWrapper wrapper,
                                 StampAlter alter,
                                 StampAlterItem item) {
        if (item.indexType == KeyIndexType.UNIQUE) {
            sb.append(" UNIQUE");
        } else if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            sb.append(" PRIMARY KEY");
        } else {
            sb.append(" INDEX");
        }

        if (StringTools.isNotEmpty(item.name)) {
            sb.append(" " + item.name);
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
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    StampAction.class, "miss_index_columns"));
        }

        if (StringTools.isNotEmpty(item.comment)) {
            sb.append(" COMMENT \"" + item.comment + "\"");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column,
                                  int type) {
        if (type == 3 || type == 2) {
            sb.append(" ALTER COLUMN");
        }
        sb.append(" " + this.getColumnName(wrapper, alter, type == 2 ? column.oldColumn : column.column));
        if (column.columnType != null) {
            if (type == 3 || type == 2) {
                sb.append(" TYPE");
            }
            sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
        }
        if (!column.nullable) {
            sb.append(" NOT NULL");
        }
        if (column.autoIncrement) {
            if (type == 3) {
                String outTableName = this.getTableName(wrapper, alter.table, alter.name, false);
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
                sb.append(" BIGSERIAL");
            }
        }
        if (column.pk) {
            sb.append(" PRIMARY KEY");
        }
        if (StringTools.isNotEmpty(column.defaultValue)) {
            sb.append(" DEFAULT \"" + column.defaultValue + "\"");
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
