package org.mimosaframework.orm.platform.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class DB2StampCreate extends DB2StampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(DB2StampCreate.class);

    public DB2StampCreate() {
        this.declareInBegin = true;
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampCreate create = (StampCreate) action;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE");
        if (create.target == KeyTarget.DATABASE) {
            sb = null;
            logger.warn("db2 can't create database in current operation");
        }
        if (create.target == KeyTarget.TABLE) {
            String tableName = this.getTableName(wrapper, create.tableClass, create.tableName);
            sb.append(" TABLE");
            if (create.checkExist) {
                this.getDeclares().add("HAS_TABLE NUMERIC");
                this.getBegins().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO HAS_TABLE FROM SYSIBM.SYSTABLES " +
                        "WHERE NAME = '" + tableName + "'"));
            }
            sb.append(" " + tableName);

            sb.append(" (");
            this.buildTableColumns(wrapper, sb, create);
            this.buildTableIndex(wrapper, sb, create);
            sb.append(")");

            if (StringTools.isNotEmpty(create.comment)) {
                this.addCommentSQL(wrapper, create, create, create.comment, 2);
            }
            if (StringTools.isNotEmpty(create.charset)) {
                logger.warn("db2 can't set table charset");
            }
            if (StringTools.isNotEmpty(create.extra)) {
                sb.append(" " + create.extra);
            }
        }
        if (create.target == KeyTarget.INDEX) {
            sb.append(" INDEX");
            sb.append(" " + RS + create.indexName + RE);
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

        if (create.target == KeyTarget.TABLE && create.checkExist) {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate
                    ("IF HAS_TABLE = 0 THEN", sb.toString(), "END IF")), null);
        } else {
            return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
        }
    }

    private void buildTableIndex(MappingGlobalWrapper wrapper, StringBuilder sb, StampCreate create) {
        StampCreateIndex[] indices = create.indices;
        if (indices != null && indices.length > 0) {
            String tableName = this.getTableName(wrapper, create.tableClass, create.tableName);
            int i = 0;
            for (StampCreateIndex index : indices) {
                if (index.indexType == KeyIndexType.PRIMARY_KEY) {
                    sb.append(",");
                    sb.append("PRIMARY KEY");
                    if (StringTools.isNotEmpty(index.name)) {
                        sb.append(" " + index.name);
                    }
                    this.setTableIndexColumn(index, sb, wrapper, create);
                }
                if (index.indexType == KeyIndexType.INDEX) {
                    StringBuilder buildIndex = new StringBuilder();
                    buildIndex.append("CREATE INDEX");
                    if (StringTools.isNotEmpty(index.name)) {
                        buildIndex.append(" " + index.name);
                    }
                    buildIndex.append(" ON ");
                    buildIndex.append(tableName);
                    this.setTableIndexColumn(index, buildIndex, wrapper, create);
                    this.getBuilders().add(new ExecuteImmediate(buildIndex));
                }
                if (index.indexType == KeyIndexType.UNIQUE) {
                    StringBuilder buildIndex = new StringBuilder();
                    buildIndex.append("CREATE INDEX ");
                    buildIndex.append("UNIQUE");
                    if (StringTools.isNotEmpty(index.name)) {
                        buildIndex.append(" " + index.name);
                    }
                    buildIndex.append(" ON ");
                    buildIndex.append(tableName);
                    this.setTableIndexColumn(index, buildIndex, wrapper, create);
                    this.getBuilders().add(new ExecuteImmediate(buildIndex));
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
