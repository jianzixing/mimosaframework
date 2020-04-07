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
            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append("ALTER DATABASE " + alter.name + " COLLATE " + alter.charset);
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
        String tableName = this.getTableName(wrapper, alter.table, alter.name);
        if (item.action == KeyAction.ADD) {
            if (item.struct == KeyAlterStruct.COLUMN) {
                sb.append("ALTER");
                sb.append(" TABLE");
                sb.append(" " + tableName);
                sb.append(" ADD");
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
            if (item.dropType == KeyAlterDropType.COLUMN) {
                sb.append("ALTER");
                sb.append(" TABLE");
                sb.append(" " + tableName);
                sb.append(" DROP");
                sb.append(" COLUMN");
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.INDEX) {
                sb.append("DROP");
                sb.append(" INDEX");
                sb.append(" " + item.name);
                sb.append(" ON " + tableName);
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                String tableOutName = this.getTableName(wrapper, alter.table, alter.name, false);
                this.getDeclares().add("@PK_INDEXNAME VARCHAR(MAX)");
                this.getBegins().add(new ExecuteImmediate()
                        .setProcedure("SELECT @PK_INDEXNAME=(SELECT A.NAME INDEXNAME " +
                                "FROM SYS.INDEXES A " +
                                "INNER JOIN SYS.INDEX_COLUMNS B ON A.OBJECT_ID = B.OBJECT_ID AND A.INDEX_ID = B.INDEX_ID " +
                                "INNER JOIN SYS.SYSINDEXKEYS C ON A.OBJECT_ID = C.ID AND B.INDEX_ID = C.INDID AND B.COLUMN_ID = C.COLID " +
                                "INNER JOIN INFORMATION_SCHEMA.COLUMNS D ON A.OBJECT_ID = OBJECT_ID(D.TABLE_NAME) AND C.KEYNO = D.ORDINAL_POSITION " +
                                "WHERE A.OBJECT_ID = OBJECT_ID('" + tableOutName + "') " +
                                "AND A.IS_PRIMARY_KEY = 1)"));
                sb.append("exec ('ALTER TABLE " + tableName + " DROP CONSTRAINT ' + @PK_INDEXNAME)");
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
            String tableOutName = this.getTableName(wrapper, alter.table, alter.name, false);
            sb.append("DBCC CHECKIDENT('" + tableOutName + "',RESEED," + item.value + ")");
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            logger.warn("sqlserver can't set table charset");
            sb.setLength(0);
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
        String tableName = this.getTableName(wrapper, alter.table, alter.name);
        if (item.indexType != KeyIndexType.PRIMARY_KEY) {
            sb.append("CREATE");
        }
        if (item.indexType == KeyIndexType.UNIQUE) {
            sb.append(" UNIQUE");
            sb.append(" INDEX");
        } else if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            sb.append("ALTER TABLE " + tableName + " ADD");
            if (StringTools.isNotEmpty(item.name)) {
                sb.append(" CONSTRAINT " + item.name);
            }
            sb.append(" PRIMARY KEY");
        } else {
            sb.append(" INDEX");
        }

        if (item.indexType != KeyIndexType.PRIMARY_KEY && StringTools.isNotEmpty(item.name)) {
            sb.append(" " + item.name);
        }

        if (item.indexType != KeyIndexType.PRIMARY_KEY) {
            sb.append(" ON");
            sb.append(" " + tableName);
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
            logger.warn("sqlserver can't set index comment");
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
            this.addCommentSQL(wrapper, alter,
                    type == 2 ? column.oldColumn : column.column, column.comment, 1, false);
        }
        if (column.after != null) {
            logger.warn("sqlserver can't set column order");
        }
        if (column.before != null) {
            logger.warn("sqlserver can't set column order");
        }
    }
}
