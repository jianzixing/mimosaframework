package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public abstract class PlatformStampSelect extends PlatformStampCommonality {
    public PlatformStampSelect(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
        share.setSelect(this);
    }

    protected abstract void buildSelect(MappingGlobalWrapper wrapper,
                                        StampSelect select,
                                        StringBuilder sb,
                                        List<SQLDataPlaceholder> placeholders);

    protected void buildFields(MappingGlobalWrapper wrapper,
                               StampSelect select,
                               StringBuilder sb,
                               boolean isUpperTableName) {
        StampSelectField[] columns = select.columns;
        int m = 0;
        for (StampSelectField column : columns) {
            this.buildSelectField(wrapper, select, column, sb, isUpperTableName);
            m++;
            if (m != columns.length) sb.append(",");
        }
    }

    protected void buildSelectForms(MappingGlobalWrapper wrapper,
                                    StampSelect select,
                                    StringBuilder sb,
                                    List<SQLDataPlaceholder> placeholders) {
        StampFrom[] froms = select.froms;
        int i = 0;
        for (StampFrom from : froms) {
            this.buildFrom(wrapper, sb, select, placeholders, from);
            if (StringTools.isNotEmpty(from.aliasName)) {
                String tableName = this.reference.getTableName(wrapper, null, from.aliasName);
                sb.append(" AS " + tableName);
            }
            i++;
            if (i != froms.length) sb.append(",");
        }
    }

    protected void buildSelectJoins(MappingGlobalWrapper wrapper,
                                    StampSelect select,
                                    StringBuilder sb,
                                    List<SQLDataPlaceholder> placeholders) {
        StampSelectJoin[] joins = select.joins;
        if (joins != null) {
            for (StampSelectJoin join : joins) {
                if (join.joinType == KeyJoinType.LEFT) {
                    sb.append(" LEFT JOIN");
                }
                if (join.joinType == KeyJoinType.INNER) {
                    sb.append(" INNER JOIN");
                }
                this.buildFrom(wrapper, sb, select, placeholders, join);
                if (StringTools.isNotEmpty(join.tableAliasName)) {
                    String tableName = this.reference.getTableName(wrapper, null, join.tableAliasName);
                    sb.append(" AS " + tableName);
                }
                if (join.on != null) {
                    sb.append(" ON ");
                    this.share.buildWhere(wrapper, placeholders, select, join.on, sb);
                }
            }
        }
    }

    protected void buildSelectWhere(MappingGlobalWrapper wrapper,
                                    StampSelect select,
                                    StringBuilder sb,
                                    List<SQLDataPlaceholder> placeholders) {
        if (select.where != null) {
            sb.append(" WHERE ");
            this.share.buildWhere(wrapper, placeholders, select, select.where, sb);
        }
    }

    protected void buildSelectGroupBy(MappingGlobalWrapper wrapper,
                                      StampSelect select,
                                      StringBuilder sb) {
        if (select.groupBy != null && select.groupBy.length > 0) {
            sb.append(" GROUP BY ");
            StampColumn[] groupBy = select.groupBy;
            if (groupBy != null) {
                int j = 0;
                for (StampColumn gb : groupBy) {
                    sb.append(this.reference.getColumnName(wrapper, select, gb));
                    j++;
                    if (j != groupBy.length) sb.append(",");
                }
            }
        }
    }

    protected void buildSelectHaving(MappingGlobalWrapper wrapper,
                                     StampSelect select,
                                     StringBuilder sb,
                                     List<SQLDataPlaceholder> placeholders) {
        if (select.having != null) {
            sb.append(" HAVING ");
            this.share.buildWhere(wrapper, placeholders, select, select.having, sb);
        }
    }

    protected void buildSelectOrderBy(MappingGlobalWrapper wrapper,
                                      StampSelect select,
                                      StringBuilder sb) {
        if (select.orderBy != null && select.orderBy.length > 0) {
            StampOrderBy[] orderBy = select.orderBy;
            sb.append(" ORDER BY ");
            int j = 0;
            for (StampOrderBy ob : orderBy) {
                sb.append(this.reference.getColumnName(wrapper, select, ob.column));
                if (ob.sortType == KeySortType.ASC)
                    sb.append(" ASC");
                else
                    sb.append(" DESC");
                j++;
                if (j != orderBy.length) sb.append(",");
            }
        }
    }

    protected void buildSelectField(MappingGlobalWrapper wrapper,
                                    StampSelect select,
                                    StampSelectField field,
                                    StringBuilder sb,
                                    boolean isUpperTableName) {
        StampColumn column = field.column;
        StampFieldFun fun = field.fun;
        String aliasName = field.aliasName;
        String tableAliasName = field.tableAliasName;

        if (field.distinct) {
            sb.append("DISTINCT ");
        }

        if (field.fieldType == KeyFieldType.ALL) {
            if (StringTools.isNotEmpty(tableAliasName)) {
                sb.append(this.reference.getWrapStart()
                        + (isUpperTableName ? tableAliasName.toUpperCase() : tableAliasName)
                        + this.reference.getWrapEnd() + ".");
            }
            sb.append("*");
        } else if (field.fieldType == KeyFieldType.COLUMN) {
            if (StringTools.isNotEmpty(tableAliasName)) {
                sb.append(this.reference.getWrapStart()
                        + (isUpperTableName ? tableAliasName.toUpperCase() : tableAliasName)
                        + this.reference.getWrapEnd() + ".");
            }
            sb.append(this.reference.getColumnName(wrapper, select, column));
            if (StringTools.isNotEmpty(aliasName)) {
                sb.append(" AS " + this.reference.getWrapStart() + aliasName + this.reference.getWrapEnd());
            }
        } else if (field.fieldType == KeyFieldType.FUN) {
            this.share.buildSelectFieldFun(wrapper, select, fun, sb);

            if (StringTools.isNotEmpty(aliasName)) {
                sb.append(" AS " + this.reference.getWrapStart() + aliasName + this.reference.getWrapEnd());
            }
        }
    }

    protected void buildFrom(MappingGlobalWrapper wrapper,
                             StringBuilder sb,
                             StampSelect select,
                             List<SQLDataPlaceholder> placeholders,

                             // StampFrom or StampSelectJoin
                             Object stamp) {
        if (stamp != null && stamp instanceof StampFrom) {
            StampFrom from = (StampFrom) stamp;
            if (from.builder != null) {
                sb.append("(");
                this.buildSelect(wrapper, from.builder, sb, placeholders);
                sb.append(")");
            } else {
                sb.append(this.reference.getTableName(wrapper, from.table, from.name));
            }
        } else if (stamp != null && stamp instanceof StampSelectJoin) {
            StampSelectJoin join = (StampSelectJoin) stamp;
            if (join.builder != null) {
                sb.append(" (");
                this.buildSelect(wrapper, select, sb, placeholders);
                sb.append(")");
            } else {
                sb.append(" " + this.reference.getTableName(wrapper, join.tableClass, join.tableName));
            }
        }
    }
}
