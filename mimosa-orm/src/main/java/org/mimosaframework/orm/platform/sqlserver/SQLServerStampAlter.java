package org.mimosaframework.orm.platform.sqlserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class SQLServerStampAlter extends SQLServerStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(SQLServerStampAlter.class);

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
            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
            if (StringTools.isNotEmpty(alter.charset)) {
                logger.warn("sqlserver can't set table charset");
            }
        }
        return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        String tableName = this.getTableName(wrapper, alter.table, alter.name);
        if (item.action == KeyAction.ADD) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" ADD");
            if (item.struct == KeyAlterStruct.COLUMN) {
                this.buildAlterColumn(sb, wrapper, alter, item, 1);
            }
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.CHANGE) {
            String tableOutName = this.getTableName(wrapper, alter.table, alter.name, false);
            String columnName = this.getColumnName(wrapper, alter, item.column, false);
            String oldColumnName = this.getColumnName(wrapper, alter, item.oldColumn, false);
            this.buildAlterColumn(sb, wrapper, alter, item, 2);
            if (!columnName.equalsIgnoreCase(oldColumnName)) {
                this.getBuilders().add(new ExecuteImmediate().setProcedure(
                        "EXEC SP_RENAME \"" + tableOutName + "." +
                                oldColumnName + "\", \"" + columnName + "\", \"COLUMN\""
                ));
            }
        }

        if (item.action == KeyAction.MODIFY) {
            this.buildAlterColumn(sb, wrapper, alter, item, 3);
        }

        if (item.action == KeyAction.DROP) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
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
            if (item.renameType == KeyAlterRenameType.COLUMN) {
                String tableOutName = this.getTableName(wrapper, alter.table, alter.name, false);
                sb.append("EXEC SP_RENAME ");
                sb.append("\"" + tableOutName + "." + this.getColumnName(wrapper, alter, item.oldColumn, false) + "\"");
                sb.append(",");
                sb.append(" \"" + this.getColumnName(wrapper, alter, item.column, false) + "\"");
                sb.append(", \"COLUMN\"");
            }
            if (item.renameType == KeyAlterRenameType.INDEX) {
                sb.append("ALTER");
                sb.append(" TABLE");
                sb.append(" " + tableName);
                sb.append(" RENAME");
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
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" AUTO_INCREMENT = " + item.value);
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" CHARACTER SET = " + item.name);
        }
        if (item.action == KeyAction.COMMENT) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" COMMENT = \"" + item.comment + "\"");
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
        String tableName = this.getTableName(wrapper, alter.table, alter.name);
        String columnName = this.getColumnName(wrapper, alter, column.column);

        if (type == 3 || type == 2) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" ALTER COLUMN");
        }
        if (type != 2) {
            sb.append(" " + columnName);
        } else {
            String oldColumnName = this.getColumnName(wrapper, alter, column.oldColumn);
            sb.append(" " + oldColumnName);
        }
        if (column.columnType != null) {
            sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
        }
        if (!column.nullable) {
            sb.append(" NOT NULL");
        }
        if (column.autoIncrement) {
            if (type == 1) {
                sb.append(" IDENTITY(1,1)");
            } else {
                logger.warn("sqlserver can't reset autoincrement column");
            }
        }
        if (column.pk) {
            sb.append(" PRIMARY KEY");
        }
        if (column.unique) {
            sb.append(" UNIQUE");
        }
        if (column.key) {
            sb.append(" KEY");
        }
        if (StringTools.isNotEmpty(column.defaultValue)) {
            sb.append(" DEFAULT \"" + column.defaultValue + "\"");
        }
        if (StringTools.isNotEmpty(column.comment)) {
            this.addCommentSQL(wrapper, alter, type == 2 ? column.oldColumn : column.column, column.comment, 1);
        }
        if (column.after != null) {
            sb.append(" AFTER " + this.getColumnName(wrapper, alter, column.after));
        }
        if (column.before != null) {
            sb.append(" BEFORE " + this.getColumnName(wrapper, alter, column.before));
        }
    }
}
