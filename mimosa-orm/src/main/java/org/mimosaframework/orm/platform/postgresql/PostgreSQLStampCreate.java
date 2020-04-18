package org.mimosaframework.orm.platform.postgresql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class PostgreSQLStampCreate extends PostgreSQLStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(PostgreSQLStampCreate.class);

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampCreate create = (StampCreate) action;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE");
        if (create.target == KeyTarget.DATABASE) {
            sb.append(" DATABASE");
            if (create.checkExist) {
                sb.append(" IF NOT EXISTS");
            }
            if (StringTools.isNotEmpty(create.databaseName)) {
                sb.append(" " + create.databaseName);
            }
            if (StringTools.isNotEmpty(create.charset)) {
                sb.append(" ENCODING = " + create.charset);
            }
            if (StringTools.isNotEmpty(create.collate)) {
                sb.append(" LC_COLLATE = " + create.collate);
            }
        }
        if (create.target == KeyTarget.TABLE) {
            sb.append(" TABLE");
            if (create.checkExist) {
                sb.append(" IF NOT EXISTS");
            }
            sb.append(" " + this.getTableName(wrapper, create.tableClass, create.tableName));

            sb.append(" (");
            this.buildTableColumns(wrapper, sb, create);
            StampCreatePrimaryKey primaryKey = create.primaryKey;
            if (primaryKey != null) {
                sb.append(",");
            }
            this.buildTableIndex(wrapper, sb, create);
            sb.append(")");

            if (StringTools.isNotEmpty(create.comment)) {
                this.addCommentSQL(wrapper, create, null, create.comment, 2);
            }
            if (StringTools.isNotEmpty(create.charset)) {
                logger.warn("postgresql can't set table charset");
            }
            if (StringTools.isNotEmpty(create.extra)) {
                sb.append(" " + create.extra);
            }
        }
        if (create.target == KeyTarget.INDEX) {
            sb.append(" INDEX");
            sb.append(" " + create.indexName);
            sb.append(" ON");
            sb.append(" " + this.getTableName(wrapper, create.tableClass, create.tableName));

            int i = 0;
            sb.append(" (");
            for (StampColumn column : create.indexColumns) {
                sb.append(this.getColumnName(wrapper, create, column));
                i++;
                if (i != create.indexColumns.length) sb.append(",");
            }
            sb.append(")");
        }
        return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
    }

    private void buildTableIndex(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreatePrimaryKey index = create.primaryKey;
        if (index != null) {
            sb.append("PRIMARY KEY");
            this.setTableIndexColumn(index, sb, wrapper, create);
        }
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
                sb.append(this.getColumnName(wrapper, create, column.column));

                if (column.autoIncrement == KeyConfirm.YES) {
                    if (column.columnType.equals(KeyColumnType.INT)) {
                        sb.append(" SERIAL");
                    } else if (column.columnType.equals(KeyColumnType.SMALLINT)
                            || column.columnType.equals(KeyColumnType.TINYINT)) {
                        sb.append(" SMALLSERIAL");
                    } else {
                        sb.append(" BIGSERIAL");
                    }
                } else {
                    sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
                }

                if (column.nullable == KeyConfirm.NO) {
                    sb.append(" NOT NULL");
                }

                if (column.pk == KeyConfirm.YES) {
                    if (primaryKeyIndex == null) primaryKeyIndex = new ArrayList<>();
                    primaryKeyIndex.add(column.column);
                }
                if (column.defaultValue != null) {
                    sb.append(" DEFAULT '" + column.defaultValue + "'");
                }
                if (StringTools.isNotEmpty(column.comment)) {
                    this.addCommentSQL(wrapper, create, column.column, column.comment, 1);
                }

                i++;
                if (i != columns.length) sb.append(",");
            }

            if (primaryKeyIndex != null && primaryKeyIndex.size() > 0) {
                StampCreatePrimaryKey pkIdx = new StampCreatePrimaryKey();
                pkIdx.columns = primaryKeyIndex.toArray(new StampColumn[]{});
                create.primaryKey = pkIdx;
            }
        }
    }
}
