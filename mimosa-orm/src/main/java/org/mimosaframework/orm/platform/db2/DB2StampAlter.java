package org.mimosaframework.orm.platform.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;


/**
 * 注意: 更改字段类型是有操作限制的. 将字段改为比之前类型长度大的可以,如果要改小,必须先drop掉原来的column,然后再重新添加.
 * 例如我要将一个Varchar(50)的column改为Varchar(30) ,这样采用以上的sql是不能成功的. 另外改为不同的类型,也需要先drop掉column.
 */
public class DB2StampAlter extends DB2StampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(DB2StampAlter.class);

    public DB2StampAlter() {
        this.declareInBegin = true;
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper,
                                           StampAction action) {
        StampAlter alter = (StampAlter) action;
        StringBuilder sb = new StringBuilder();
        if (alter.target == KeyTarget.DATABASE) {
            if (StringTools.isNotEmpty(alter.charset)) {
                sb = null;
                logger.warn("db2 can't reset database charset");
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
            if (StringTools.isNotEmpty(alter.charset)) {
                logger.warn("db2 can't reset table charset");
            }
        }
        String sql = sb.toString();
        if (StringTools.isNotEmpty(sql) && this.multiExecuteImmediate()) {
            sql = sql.replaceAll("'", "''");
        }
        return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sql)), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        if (item.action == KeyAction.ADD) {
            if (item.struct == KeyAlterStruct.COLUMN) {
                sb.append("ALTER TABLE");
                String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);
                sb.append(" " + tableName);
                sb.append(" ADD COLUMN");
                this.buildAddAlterColumn(sb, wrapper, alter, item);
            }
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.MODIFY) {
            sb.setLength(0);
            String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);
            this.buildAlterColumn(wrapper, alter, item, 3);
            this.getBuilders().add(new ExecuteImmediate().setProcedure
                    ("call sysproc.admin_cmd ('reorg table db2inst1." + tableName + "')"));
        }

        if (item.action == KeyAction.DROP) {
            String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);

            if (item.dropType == KeyAlterDropType.COLUMN) {
                sb.append("ALTER TABLE");
                sb.append(" " + tableName);
                sb.append(" DROP");
                sb.append(" COLUMN");
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.append("ALTER TABLE");
                sb.append(" " + tableName);
                sb.append(" DROP");
                sb.append(" PRIMARY KEY");
            }
        }

        if (item.action == KeyAction.AUTO_INCREMENT) {
            this.getDeclares().add("IDENTITY_NAME VARCHAR(2000)");
            this.getBegins().add(new ExecuteImmediate().setProcedure("SELECT NAME INTO IDENTITY_NAME " +
                    "FROM SYSIBM.SYSCOLUMNS WHERE TBNAME='T_USER' AND IDENTITY='Y'"));

            sb.append("ALTER TABLE");
            String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);
            sb.append(" " + tableName);
            sb.append(" ALTER COLUMN \"'||IDENTITY_NAME||'\" RESTART WITH " + item.value);
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            logger.warn("db2 can't set table charset");
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
        if (item.indexType != KeyIndexType.PRIMARY_KEY) {
            sb.append("CREATE");
        }
        if (item.indexType == KeyIndexType.UNIQUE) {
            sb.append(" UNIQUE");
            sb.append(" INDEX");
        } else if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            sb.append("ALTER TABLE ");
            sb.append(tableName);
            sb.append(" ADD CONSTRAINT");
        } else {
            sb.append(" INDEX");
        }

        if (StringTools.isNotEmpty(item.indexName)) {
            sb.append(" " + RS + item.indexName + RE);
        }

        if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            if (StringTools.isEmpty(item.indexName)) {
                StringBuilder indexName = new StringBuilder();
                int m = 0;
                for (StampColumn column : item.columns) {
                    indexName.append(this.getColumnName(wrapper, alter,
                            new StampColumn(column.column), false).toUpperCase());
                    m++;
                    if (m != item.columns.length) indexName.append("_");
                }
                sb.append(" " + RS + tableName.toUpperCase() + "_" + indexName.toString() + RE);
            }
            sb.append(" PRIMARY KEY");
        } else {
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
            throw new IllegalArgumentException(I18n.print("miss_index_columns"));
        }

        if (StringTools.isNotEmpty(item.comment)) {
            logger.warn("db2 can't set index comment");
        }
    }

    private void buildAddAlterColumn(StringBuilder sb,
                                     MappingGlobalWrapper wrapper,
                                     StampAlter alter,
                                     StampAlterItem column) {
        if (column.timeForUpdate) {
            sb.append(" " + this.getColumnType(KeyColumnType.TIMESTAMP, column.len, column.scale));
            sb.append(" NOT NULL DEFAULT CURRENT_TIMESTAMP");
        } else {
            String columnName = this.getColumnName(wrapper, alter, column.column);
            sb.append(" " + columnName);

            if (column.columnType != null) {
                sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
            }
            if (column.nullable == KeyConfirm.NO) {
                sb.append(" NOT NULL");
            } else if (column.nullable == KeyConfirm.YES) {
                sb.append(" NULL");
            }

            if (column.autoIncrement == KeyConfirm.YES) {
                sb.append(" GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1)");
            }
            if (column.pk == KeyConfirm.YES) {
                sb.append(" PRIMARY KEY");
            }

            if (column.defaultValue == null) {
                sb.append(" DEFAULT");
            } else {
                sb.append(" DEFAULT ''" + column.defaultValue + "''");
            }
            if (StringTools.isNotEmpty(column.comment)) {
                this.addCommentSQL(wrapper, alter, column.column, column.comment, 1);
            }
            if (column.after != null) {
                logger.warn("db2 can't set column order");
            }
            if (column.before != null) {
                logger.warn("db2 can't set column order");
            }
        }
    }

    private void buildAlterColumn(MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column, int type) {
        StringBuilder actionSql = new StringBuilder();
        String tableName = this.getTableName(wrapper, alter.tableClass, alter.tableName);

        String columnName = this.getColumnName(wrapper, alter, type == 2 ? column.oldColumn : column.column);
        actionSql.append(" ALTER COLUMN");
        actionSql.append(" " + columnName);

        if (column.columnType != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(actionSql);
            sb.append(" SET");

            sb.append(" DATA TYPE " + this.getColumnType(column.columnType, column.len, column.scale));
            this.getBuilders().add(new ExecuteImmediate(sb));
        }
        if (column.nullable == KeyConfirm.NO) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(actionSql);
            sb.append(" SET");
            sb.append(" NOT NULL");
            this.getBuilders().add(new ExecuteImmediate(sb));
        } else if (column.nullable == KeyConfirm.YES) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(actionSql);
            sb.append(" DROP NOT");
            sb.append(" NULL");
            this.getBuilders().add(new ExecuteImmediate(sb));
        }

        if (column.autoIncrement == KeyConfirm.YES) {
            StringBuilder sb = new StringBuilder();
            this.getDeclares().add("IS_AUTO NUMERIC");
            this.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO IS_AUTO " +
                    "FROM SYSIBM.SYSCOLUMNS WHERE TBNAME='" + tableName + "' AND IDENTITY='Y'"));
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(actionSql);
            sb.append(" SET");
            sb.append(" GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1)");
            this.getBuilders().add(new ExecuteImmediate("IF IS_AUTO=0 THEN",
                    sb.toString(), "END IF"));
        } else if (column.autoIncrement == KeyConfirm.NO) {
            //
        }

        if (column.pk == KeyConfirm.YES) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(actionSql);
            sb.append(" SET");
            sb.append(" PRIMARY KEY");
            this.getBuilders().add(new ExecuteImmediate(sb));
        } else if (column.pk == KeyConfirm.NO) {
            //
        }

        if (column.defaultValue != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(actionSql);
            sb.append(" SET");
            if (column.defaultValue.equals("*****")) {
                sb.append(" DEFAULT");
            } else {
                sb.append(" DEFAULT ''" + column.defaultValue + "''");
            }

            this.getBuilders().add(new ExecuteImmediate(sb));
        }

        if (column.comment != null) {
            if (type == 2) {
                this.addCommentSQL(wrapper, alter, column.oldColumn, column.comment, 1);
            } else {
                this.addCommentSQL(wrapper, alter, column.column, column.comment, 1);
            }
        }

        if (column.after != null) {
            logger.warn("db2 can't set column order");
        }
        if (column.before != null) {
            logger.warn("db2 can't set column order");
        }
    }
}
