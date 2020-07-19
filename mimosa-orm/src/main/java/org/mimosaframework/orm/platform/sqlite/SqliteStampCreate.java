package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class SqliteStampCreate extends PlatformStampCreate {
    private static final Log logger = LogFactory.getLog(SqliteStampCreate.class);

    public SqliteStampCreate(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampCreate create = (StampCreate) action;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE");
        if (create.target == KeyTarget.DATABASE) {
            sb.setLength(0);
            logger.warn("sqlite can't create database");
        }
        if (create.target == KeyTarget.TABLE) {
            sb.append(" TABLE");
            if (create.checkExist) {
                sb.append(" IF NOT EXISTS");
            }
            sb.append(" " + this.reference.getTableName(wrapper, create.tableClass, create.tableName));

            sb.append(" (");
            this.buildTableColumns(wrapper, sb, create);
            StampCreatePrimaryKey primaryKey = create.primaryKey;
            if (primaryKey != null) {
                sb.append(",");
            }
            this.buildTableIndex(wrapper, sb, create);
            sb.append(")");

            if (StringTools.isNotEmpty(create.comment)) {
                logger.warn("sqlite can't set table comment");
            }
            if (StringTools.isNotEmpty(create.charset)) {
                logger.warn("sqlite can't set table charset");
            }
            if (StringTools.isNotEmpty(create.extra)) {
                sb.append(" " + create.extra);
            }
        }
        if (create.target == KeyTarget.INDEX) {
            if (create.indexType == KeyIndexType.UNIQUE) {
                sb.append(" UNIQUE");
            }
            sb.append(" INDEX");
            sb.append(" " + create.indexName);
            sb.append(" ON");
            sb.append(" " + this.reference.getTableName(wrapper, create.tableClass, create.tableName));

            int i = 0;
            sb.append(" (");
            for (StampColumn column : create.indexColumns) {
                sb.append(this.reference.getColumnName(wrapper, create, column));
                i++;
                if (i != create.indexColumns.length) sb.append(",");
            }
            sb.append(")");
        }
        return new SQLBuilderCombine(sb.toString(), null);
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
            sb.append(this.reference.getColumnName(wrapper, create, column));
            j++;
            if (j != columns.length) sb.append(",");
        }
        sb.append(")");
    }

    private void buildTableColumns(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateColumn[] columns = create.columns;
        if (columns != null && columns.length > 0) {
            int i = 0, pkCount = 0;

            for (StampCreateColumn column : columns) {
                if (column.pk == KeyConfirm.YES) {
                    pkCount++;
                }
            }
            List<StampColumn> primaryKeyIndex = null;
            for (StampCreateColumn column : columns) {
                sb.append(this.reference.getColumnName(wrapper, create, column.column));

                sb.append(" " + this.share.getColumnType(column.columnType, column.len, column.scale));

                if (column.nullable == KeyConfirm.NO) {
                    sb.append(" NOT NULL");
                }

                if (pkCount <= 1 && column.pk == KeyConfirm.YES) {
                    sb.append(" PRIMARY KEY");
                } else {
                    if (pkCount > 1) {
                        if (primaryKeyIndex == null) primaryKeyIndex = new ArrayList<>();
                        primaryKeyIndex.add(column.column);
                    }
                }
                if (column.autoIncrement == KeyConfirm.YES) {
                    sb.append(" AUTOINCREMENT");
                }
                if (column.defaultValue != null) {
                    sb.append(" DEFAULT \"" + column.defaultValue + "\"");
                }
                if (StringTools.isNotEmpty(column.comment)) {
                    logger.warn("sqlite can't set column comment");
                }

                i++;
                if (i != columns.length) sb.append(",");
            }

            if (primaryKeyIndex != null && primaryKeyIndex.size() > 1) {
                StampCreatePrimaryKey pkIdx = new StampCreatePrimaryKey();
                pkIdx.columns = primaryKeyIndex.toArray(new StampColumn[]{});
                create.primaryKey = pkIdx;
            }
        }
    }
}
