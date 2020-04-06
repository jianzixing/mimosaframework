package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class DB2StampCreate extends DB2StampCommonality implements StampCombineBuilder {
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
            if (StringTools.isNotEmpty(create.name)) {
                sb.append(" " + create.name);
            }
            if (StringTools.isNotEmpty(create.charset)) {
                sb.append(" " + create.charset);
            }
            if (StringTools.isNotEmpty(create.collate)) {
                sb.append(" " + create.collate);
            }
        }
        if (create.target == KeyTarget.TABLE) {
            sb.append(" TABLE");
            if (create.checkExist) {
                sb.append(" IF NOT EXISTS");
            }
            sb.append(" " + this.getTableName(wrapper, create.table, create.name));

            sb.append(" (");
            this.buildTableColumns(wrapper, sb, create);
            StampCreateIndex[] indices = create.indices;
            if (indices != null && indices.length > 0) {
                sb.append(",");
            }
            this.buildTableIndex(wrapper, sb, create);
            sb.append(")");

            if (StringTools.isNotEmpty(create.comment)) {
                sb.append(" COMMENT=\"" + create.comment + "\"");
            }
            if (StringTools.isNotEmpty(create.charset)) {
                sb.append(" CHARSET " + create.charset);
            }
            if (StringTools.isNotEmpty(create.extra)) {
                sb.append(" " + create.extra);
            }
        }
        if (create.target == KeyTarget.INDEX) {
            sb.append(" INDEX");
            sb.append(" " + create.indexName);
            sb.append(" ON");
            sb.append(" " + this.getTableName(wrapper, create.table, create.name));

            int i = 0;
            sb.append(" (");
            for (StampColumn column : create.indexColumns) {
                sb.append(this.getColumnName(wrapper, create, column));
                i++;
                if (i != create.indexColumns.length) sb.append(",");
            }
            sb.append(")");
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }

    private void buildTableIndex(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateIndex[] indices = create.indices;
        if (indices != null && indices.length > 0) {
            int i = 0;
            for (StampCreateIndex index : indices) {
                if (index.indexType == KeyIndexType.PRIMARY_KEY) {
                    sb.append("PRIMARY KEY");
                    this.setTableIndexColumn(index, sb, wrapper, create);
                }
                if (index.indexType == KeyIndexType.INDEX) {
                    sb.append("INDEX");
                    this.setTableIndexColumn(index, sb, wrapper, create);
                }
                if (index.indexType == KeyIndexType.UNIQUE) {
                    sb.append("UNIQUE");
                    this.setTableIndexColumn(index, sb, wrapper, create);
                }
                i++;
                if (i != indices.length) sb.append(",");
            }
        }
    }

    private void setTableIndexColumn(StampCreateIndex index,
                                     StringBuilder sb,
                                     MappingGlobalWrapper wrapper,
                                     StampCreate create) {
        if (StringTools.isNotEmpty(index.name)) {
            sb.append(" " + index.name);
        } else {
            sb.append(" ");
        }
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
    }

    private void buildTableColumns(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateColumn[] columns = create.columns;
        if (columns != null && columns.length > 0) {
            int i = 0;
            for (StampCreateColumn column : columns) {
                sb.append(this.getColumnName(wrapper, create, column.column));

                sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));

                if (!column.nullable) {
                    sb.append(" NOT NULL");
                }
                if (column.autoIncrement) {
                    sb.append(" GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1)");
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
                    sb.append(" COMMENT \"" + column.comment + "\"");
                }

                i++;
                if (i != columns.length) sb.append(",");
            }
        }
    }
}
