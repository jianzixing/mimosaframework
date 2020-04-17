package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class OracleStampCreate extends OracleStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(OracleStampCreate.class);

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampCreate create = (StampCreate) action;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE");
        if (create.target == KeyTarget.DATABASE) {
            sb = null;
            logger.warn("oracle can't create database by current sql");
        }
        if (create.target == KeyTarget.TABLE) {
            sb.append(" TABLE");
            String tableName = this.getTableName(wrapper, create.tableClass, create.tableName);
            if (create.checkExist) {
                this.getDeclares().add("HAS_TABLE NUMBER");
                this.getBegins().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO HAS_TABLE FROM USER_TABLES " +
                        "WHERE TABLE_NAME = '" + tableName + "'"));
            }
            sb.append(" " + tableName);

            sb.append(" (");
            this.buildTableColumns(wrapper, sb, create);
            StampCreatePrimaryKey primaryKey = create.primaryKey;
            if (primaryKey != null) {
                sb.append(",");
            }
            this.buildTableIndex(wrapper, sb, create);
            sb.append(")");


            if (StringTools.isNotEmpty(create.comment)) {
                this.addCommentSQL(wrapper, create, create, create.comment, 2);
            }

            if (StringTools.isNotEmpty(create.charset)) {
                logger.warn("oracle can't set table charset");
            }
            if (StringTools.isNotEmpty(create.extra)) {
                sb.append(" " + create.extra);
            }
        }
        if (create.target == KeyTarget.INDEX) {
            if (create.indexType == KeyIndexType.UNIQUE) {
                sb.append(" UNIQUE");
            } else {
                sb.append(" INDEX");
            }
            sb.append(" " + RS + create.indexName + RE);
            sb.append(" ON");
            sb.append(" " + this.getTableName(wrapper, create.tableClass, create.tableName));

            List<String> fullTextIndexNames = new ArrayList<>();
            int i = 0;
            sb.append(" (");
            for (StampColumn column : create.indexColumns) {
                column.table = null;
                column.tableAliasName = null;
                sb.append(this.getColumnName(wrapper, create, column));
                fullTextIndexNames.add(this.getColumnName(wrapper, create, column, false));
                i++;
                if (i != create.indexColumns.length) sb.append(",");
            }
            sb.append(")");
        }

        String createSql = sb.toString();
        if (StringTools.isNotEmpty(createSql) && this.multiExecuteImmediate()) {
            createSql = createSql.replaceAll("'", "''");
        }

        if (create.target == KeyTarget.TABLE && create.checkExist) {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate("IF HAS_TABLE = 0 THEN",
                    sb != null ? createSql : "", "END IF")), null);
        } else {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(createSql)), null);
        }
    }

    private void buildTableIndex(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreatePrimaryKey primaryKey = create.primaryKey;
        sb.append("PRIMARY KEY");
        this.setTableIndexColumn(primaryKey, sb, wrapper, create);
    }

    private void setTableIndexColumn(StampCreatePrimaryKey index,
                                     StringBuilder sb,
                                     MappingGlobalWrapper wrapper,
                                     StampCreate create) {
        sb.append("(");
        StampColumn[] columns = index.columns;
        int j = 0;
        for (StampColumn column : columns) {
            // 创建表所以不需要别名
            column.table = null;
            column.tableAliasName = null;
            sb.append(this.getColumnName(wrapper, create, column));
            j++;
            if (j != columns.length) sb.append(",");
        }
        sb.append(")");
    }

    private void buildTableColumns(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateColumn[] columns = create.columns;
        if (columns != null && columns.length > 0) {
            int i = 0;

            List<StampColumn> primaryKeyIndex = null;
            for (StampCreateColumn column : columns) {
                String columnName = this.getColumnName(wrapper, create, column.column);
                sb.append(columnName);

                sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));

                if (column.autoIncrement == KeyConfirm.YES) {
                    this.addAutoIncrement(wrapper, create.tableClass, create.tableName);
                }
                if (column.pk == KeyConfirm.YES) {
                    if (primaryKeyIndex == null) primaryKeyIndex = new ArrayList<>();
                    primaryKeyIndex.add(column.column);
                }
                if (StringTools.isNotEmpty(column.defaultValue)) {
                    sb.append(" DEFAULT '" + column.defaultValue + "'");
                }
                if (column.nullable == KeyConfirm.NO) {
                    sb.append(" NOT NULL");
                }

                if (StringTools.isNotEmpty(column.comment)) {
                    this.addCommentSQL(wrapper, create, column.column, column.comment, 1);
                }

                i++;
                if (i != columns.length) sb.append(",");
            }

            StampCreatePrimaryKey pkIdx = new StampCreatePrimaryKey();
            pkIdx.columns = primaryKeyIndex.toArray(new StampColumn[]{});
            create.primaryKey = pkIdx;
        }
    }
}
