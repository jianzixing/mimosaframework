package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class MysqlStampCreate extends MysqlAbstractStamp implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampCreate create = (StampCreate) action;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE");
        if (create.target == KeyTarget.DATABASE) {
            sb.append(" DATABASE");
            if (create.checkExist) {
                sb.append(" IF NOT EXIST");
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
                sb.append(" IF NOT EXIST");
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
        return null;
    }

    private void buildTableIndex(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateIndex[] indices = create.indices;
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
            if (index.indexType == KeyIndexType.FULLTEXT) {
                sb.append("FULLTEXT INDEX");
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
            sb.append(this.getColumnName(wrapper, create, column));
            j++;
            if (j != columns.length) sb.append(",");
        }
    }

    private void buildTableColumns(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateColumn[] columns = create.columns;
        int i = 0;
        for (StampCreateColumn column : columns) {
            sb.append(" " + this.RS + this.getColumnName(wrapper, create, column.column) + this.RE);
            if (column.columnType == KeyColumnType.INT) {
                sb.append(" INT");
            }
            if (column.columnType == KeyColumnType.VARCHAR) {
                sb.append(" VARCHAR");
            }
            if (column.columnType == KeyColumnType.CHAR) {
                sb.append(" CHAR");
            }
            if (column.columnType == KeyColumnType.BLOB) {
                sb.append(" BLOB");
            }
            if (column.columnType == KeyColumnType.TEXT) {
                sb.append(" TEXT");
            }
            if (column.columnType == KeyColumnType.TINYINT) {
                sb.append(" TINYINT");
            }
            if (column.columnType == KeyColumnType.SMALLINT) {
                sb.append(" SMALLINT");
            }
            if (column.columnType == KeyColumnType.MEDIUMINT) {
                sb.append(" MEDIUMINT");
            }
            if (column.columnType == KeyColumnType.BIT) {
                sb.append(" BIT");
            }
            if (column.columnType == KeyColumnType.BIGINT) {
                sb.append(" BIGINT");
            }
            if (column.columnType == KeyColumnType.FLOAT) {
                sb.append(" FLOAT");
            }
            if (column.columnType == KeyColumnType.DOUBLE) {
                sb.append(" DOUBLE");
            }
            if (column.columnType == KeyColumnType.DECIMAL) {
                sb.append(" DECIMAL");
            }
            if (column.columnType == KeyColumnType.BOOLEAN) {
                sb.append(" BOOLEAN");
            }
            if (column.columnType == KeyColumnType.DATE) {
                sb.append(" DATE");
            }
            if (column.columnType == KeyColumnType.TIME) {
                sb.append(" TIME");
            }
            if (column.columnType == KeyColumnType.DATETIME) {
                sb.append(" DATETIME");
            }
            if (column.columnType == KeyColumnType.TIMESTAMP) {
                sb.append(" TIMESTAMP");
            }
            if (column.columnType == KeyColumnType.YEAR) {
                sb.append(" YEAR");
            }

            if (!column.nullable) {
                sb.append(" NOT NULL");
            }
            if (!column.autoIncrement) {
                sb.append(" AUTO_INCREMENT");
            }
            if (!column.pk) {
                sb.append(" PRIMARY KEY");
            }
            if (!column.unique) {
                sb.append(" UNIQUE");
            }
            if (!column.key) {
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
