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
            sb.append(")");

            this.buildTableIndex(wrapper, sb, create);

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

        if (create.target == KeyTarget.TABLE && create.checkExist) {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate("IF HAS_TABLE = 0 THEN",
                    sb != null ? sb.toString() : "", "END IF")), null);
        } else {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
        }
    }

    private void buildTableIndex(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateIndex[] indices = create.indices;
        if (indices != null && indices.length > 0) {
            int i = 0;
            boolean isNeedDeclare = false;
            for (StampCreateIndex index : indices) {
                if (index.indexType == KeyIndexType.PRIMARY_KEY) {
                    sb.append("PRIMARY KEY");
                    if (this.setTableIndexColumn(index, 1, wrapper, create)) isNeedDeclare = true;
                }
                if (index.indexType == KeyIndexType.INDEX) {
                    if (this.setTableIndexColumn(index, 2, wrapper, create)) isNeedDeclare = true;
                }
                if (index.indexType == KeyIndexType.UNIQUE) {
                    sb.append("UNIQUE");
                    if (this.setTableIndexColumn(index, 4, wrapper, create)) isNeedDeclare = true;
                }
                i++;
                if (i != indices.length) sb.append(",");
            }

            if (isNeedDeclare) {
                this.getDeclares().add("HAS_INDEX NUMBER");
            }
        }
    }

    private boolean setTableIndexColumn(StampCreateIndex index,
                                        int type,
                                        MappingGlobalWrapper wrapper,
                                        StampCreate create) {
        boolean isNeedCheck = false;
        String tableName = this.getTableName(wrapper, create.tableClass, create.tableName);
        StringBuilder sb = new StringBuilder();
        StampColumn[] columns = index.columns;

        StringBuilder queryNames = new StringBuilder();
        StringBuilder queryNames2 = new StringBuilder();
        int j = 0;
        for (StampColumn column : columns) {
            // 创建表所以不需要别名
            column.table = null;
            column.tableAliasName = null;
            queryNames.append(this.getColumnName(wrapper, create, column));
            queryNames2.append("'" + this.getColumnName(wrapper, create, column, false) + "'");
            j++;
            if (j != columns.length) {
                queryNames.append(",");
                queryNames2.append(",");
            }
        }
        sb.append("CREATE");
        if (type == 2) {
            sb.append(" INDEX");
        }
        if (StringTools.isNotEmpty(index.name)) {
            sb.append(" " + index.name + " ");
            this.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO HAS_INDEX FROM " +
                    "USER_INDEXES T1,USER_IND_COLUMNS T2 " +
                    "WHERE T1.TABLE_NAME='" + tableName + "' " +
                    "AND T1.INDEX_NAME='" + index.name + "' " +
                    "AND T1.TABLE_NAME = T2.TABLE_NAME " +
                    "AND T1.INDEX_NAME = T2.INDEX_NAME " +
                    "AND T2.COLUMN_NAME in (" + queryNames2 + ")"));
            isNeedCheck = true;
        } else {
            sb.append(" ");
        }

        sb.append("ON ");
        sb.append(tableName + " ");
        sb.append("(");
        sb.append(queryNames);
        sb.append(")");

        this.getBuilders().add(new ExecuteImmediate("IF HAS_INDEX = " + columns.length + " THEN", sb.toString(), "END IF"));
        return isNeedCheck;
    }

    private void buildTableColumns(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateColumn[] columns = create.columns;
        if (columns != null && columns.length > 0) {
            int i = 0;
            for (StampCreateColumn column : columns) {
                String columnName = this.getColumnName(wrapper, create, column.column);
                sb.append(columnName);

                sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));

                if (!column.nullable) {
                    sb.append(" NOT NULL");
                }
                if (column.autoIncrement) {
                    this.addAutoIncrement(wrapper, create.tableClass, create.tableName);
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
                            newIndex[ii].name = column.column.column.toString();
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
