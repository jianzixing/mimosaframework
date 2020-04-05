package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class OracleStampCreate extends OracleStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(OracleStampCreate.class);

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
            String tableName = this.getTableName(wrapper, create.table, create.name);
            if (create.checkExist) {
                this.getDeclares().add("HAS_TABLE NUMBER");
                this.getBegins().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO HAS_TABLE FROM USER_TABLES " +
                        "WHERE TABLE_NAME = '" + tableName + "'"));
            }
            sb.append(" " + tableName);

            sb.append(" (");
            this.buildTableColumns(wrapper, sb, create);
            StampCreateIndex[] indices = create.indices;
            if (indices != null && indices.length > 0) {
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

        if (create.target == KeyTarget.TABLE && create.checkExist) {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate("IF HAS_TABLE = 0 THEN",
                    sb != null ? sb.toString() : "", "END IF")), null);
        } else {
            return new SQLBuilderCombine(sb.toString(), null);
        }
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
        sb.append("(");
        for (StampColumn column : columns) {
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
            for (StampCreateColumn column : columns) {
                String columnName = this.getColumnName(wrapper, create, column.column);
                sb.append(" " + columnName);

                sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));

                if (!column.nullable) {
                    sb.append(" NOT NULL");
                }
                if (column.autoIncrement) {
                    this.addAutoIncrement(wrapper, create.table, create.name);
                }
                if (column.pk) {
                    sb.append(" PRIMARY KEY");
                }
                if (column.unique) {
                    sb.append(" UNIQUE");
                }
                if (column.key) {
                    StampCreateIndex[] indices = create.indices;
                    int len = 0;
                    if (indices != null) len = indices.length;
                    StampCreateIndex[] newIndex = new StampCreateIndex[len + 1];
                    for (int ii = 0; ii < len + 1; ii++) {
                        if (ii < len) {
                            newIndex[ii] = indices[ii];
                        } else {
                            newIndex[ii] = new StampCreateIndex();
                            newIndex[ii].indexType = KeyIndexType.INDEX;
                            newIndex[ii].columns = new StampColumn[]{column.column};
                        }
                    }
                    create.indices = newIndex;
                }
                if (StringTools.isNotEmpty(column.defaultValue)) {
                    sb.append(" DEFAULT \"" + column.defaultValue + "\"");
                }
                if (StringTools.isNotEmpty(column.comment)) {
                    this.addCommentSQL(wrapper, create, column.column, column.comment, 1);
                }

                i++;
                if (i != columns.length) sb.append(",");
            }
        }
    }
}
