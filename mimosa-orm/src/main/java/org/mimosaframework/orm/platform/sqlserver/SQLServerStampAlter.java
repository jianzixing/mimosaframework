package org.mimosaframework.orm.platform.sqlserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

public class SQLServerStampAlter extends PlatformStampAlter {
    private static final Log logger = LogFactory.getLog(SQLServerStampAlter.class);

    public SQLServerStampAlter(PlatformStampSection section,
                               PlatformStampReference reference,
                               PlatformDialect dialect,
                               PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper,
                                           StampAction action) {
        StampAlter alter = (StampAlter) action;
        StringBuilder sb = new StringBuilder();
        if (alter.target == KeyTarget.DATABASE) {
            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append("ALTER DATABASE " + alter.databaseName + " COLLATE " + alter.charset);
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
        }
        return new SQLBuilderCombine(this.section.toSQLString(new ExecuteImmediate(sb)), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        String tableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName);
        if (item.action == KeyAction.ADD) {
            if (item.struct == KeyAlterStruct.COLUMN) {
                sb.append("ALTER");
                sb.append(" TABLE");
                sb.append(" " + tableName);
                sb.append(" ADD");
                this.buildAddAlterColumn(sb, wrapper, alter, item);
            }
            if (item.struct == KeyAlterStruct.PRIMARY_KEY) {
                this.buildAddPrimaryKey(sb, wrapper, alter, item);
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
                sb.append(" " + this.reference.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                String tableOutName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName, false);
                this.section.getDeclares().add("@PK_INDEXNAME VARCHAR(MAX)");
                this.section.getBegins().add(new ExecuteImmediate()
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

        if (item.action == KeyAction.AUTO_INCREMENT) {
            String tableOutName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName, false);
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

    private void buildAddPrimaryKey(StringBuilder sb,
                                    MappingGlobalWrapper wrapper,
                                    StampAlter alter,
                                    StampAlterItem item) {
        String tableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName);
        String outTableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName, false);

        sb.append("ALTER TABLE " + tableName + " ADD");
        if (StringTools.isNotEmpty(item.indexName)) {
            sb.append(" CONSTRAINT " + item.indexName);
        } else {
            sb.append(" CONSTRAINT " + outTableName.toUpperCase() + "_PKEY");
        }
        sb.append(" PRIMARY KEY");
        if (item.columns != null) {
            sb.append(" (");
            int i = 0;
            for (StampColumn column : item.columns) {
                sb.append(this.reference.getColumnName(wrapper, alter, column));
                i++;
                if (i != item.columns.length) sb.append(",");
            }
            sb.append(")");
        } else {
            throw new IllegalArgumentException(I18n.print("miss_index_columns"));
        }

        if (StringTools.isNotEmpty(item.comment)) {
            logger.warn("sqlserver can't set index comment");
        }
    }

    private void buildAddAlterColumn(StringBuilder sb,
                                     MappingGlobalWrapper wrapper,
                                     StampAlter alter,
                                     StampAlterItem column) {
        String tableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName);
        String columnName = this.reference.getColumnName(wrapper, alter, column.column);

        sb.append(" " + columnName);
        if (column.columnType != null) {
            sb.append(" " + this.share.getColumnType(column.columnType, column.len, column.scale));
        }
        if (column.nullable == KeyConfirm.NO) {
            sb.append(" NOT NULL");
        }
        if (column.autoIncrement == KeyConfirm.YES) {
            sb.append(" IDENTITY(1,1)");
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
            this.share.addCommentSQL(wrapper, alter, column.column, column.comment, 1, false);
        }
        if (column.after != null) {
            logger.warn("sqlserver can't set column order");
        }
        if (column.before != null) {
            logger.warn("sqlserver can't set column order");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column,
                                  int type) {
        String tableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName);
        String columnName = this.reference.getColumnName(wrapper, alter, column.column);

        if (type == 3 || type == 2) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" ALTER COLUMN");
        }
        if (type != 2) {
            sb.append(" " + columnName);
        } else {
            String oldColumnName = this.reference.getColumnName(wrapper, alter, column.oldColumn);
            sb.append(" " + oldColumnName);
        }
        if (column.columnType != null) {
            sb.append(" " + this.share.getColumnType(column.columnType, column.len, column.scale));
        }
        if (column.nullable == KeyConfirm.NO) {
            sb.append(" NOT NULL");
        }
        if (column.autoIncrement == KeyConfirm.YES) {
            if (type == 1) {
                sb.append(" IDENTITY(1,1)");
            } else {
                logger.warn("sqlserver can't reset autoincrement column");
            }
        }
        if (column.pk == KeyConfirm.YES) {
            sb.append(" PRIMARY KEY");
        }
        if (column.defaultValue != null) {
            sb.setLength(0);
            if (column.defaultValue.equals("*****")) {
                sb.append("ALTER TABLE " + tableName + " ADD DEFAULT (NULL) FOR " + columnName + " WITH VALUES");
            } else {
                sb.append("ALTER TABLE " + tableName + " ADD DEFAULT ('" + column.defaultValue + "') FOR " + columnName + " WITH VALUES");
            }
        }
        if (StringTools.isNotEmpty(column.comment)) {
            this.share.addCommentSQL(wrapper, alter,
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
