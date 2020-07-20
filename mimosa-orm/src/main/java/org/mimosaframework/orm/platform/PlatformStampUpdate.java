package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public abstract class PlatformStampUpdate extends PlatformStampCommonality {
    public PlatformStampUpdate(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampUpdate update = (StampUpdate) action;
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        sb.append("UPDATE ");
        this.buildUpdateFrom(wrapper, update, sb);

        sb.append(" SET ");
        this.buildUpdateSet(wrapper, update, sb, placeholders);
        this.buildUpdateWhere(wrapper, update, sb, placeholders);

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    protected void buildUpdateFrom(MappingGlobalWrapper wrapper,
                                   StampUpdate update,
                                   StringBuilder sb) {
        StampFrom fromTarget = update.table;
        if (fromTarget != null) {
            sb.append(this.reference.getTableName(wrapper, fromTarget.table, fromTarget.name));
            if (StringTools.isNotEmpty(fromTarget.aliasName)) {
                sb.append(" AS " + this.reference.getWrapStart() + fromTarget.aliasName + this.reference.getWrapEnd());
            }
        }
    }

    protected void buildUpdateSet(MappingGlobalWrapper wrapper,
                                  StampUpdate update,
                                  StringBuilder sb,
                                  List<SQLDataPlaceholder> placeholders) {
        StampUpdateItem[] items = update.items;
        int k = 0;
        for (StampUpdateItem item : items) {
            this.buildUpdateItem(wrapper, update, item, sb, placeholders);
            k++;
            if (k != items.length) sb.append(",");
        }
    }

    protected void buildUpdateWhere(MappingGlobalWrapper wrapper,
                                    StampUpdate update,
                                    StringBuilder sb,
                                    List<SQLDataPlaceholder> placeholders) {
        if (update.where != null) {
            sb.append(" WHERE ");
            this.share.buildWhere(wrapper, placeholders, update, update.where, sb);
        }
    }

    protected void buildUpdateItem(MappingGlobalWrapper wrapper,
                                   StampUpdate update,
                                   StampUpdateItem item,
                                   StringBuilder sb,
                                   List<SQLDataPlaceholder> placeholders) {
        item.column.table = null;
        item.column.tableAliasName = null;
        String name = this.reference.getColumnName(wrapper, update, item.column);
        sb.append(name);
        sb.append(" = ");

        if (item.value instanceof StampColumn) {
            StampColumn column = (StampColumn) item.value;
            column.table = null;
            column.tableAliasName = null;
            sb.append(this.reference.getColumnName(wrapper, update, column));
        } else if (item.value instanceof StampFormula) {
            StampFormula formula = (StampFormula) item.value;
            if (formula != null && formula.formulas != null) {
                StampFormula.Formula[] formulas = formula.formulas;
                boolean hasFirst = false;
                for (int i = 0; i < formulas.length; i++) {
                    StampFormula.Formula fml = formulas[i];
                    if (fml.column != null || fml.value != null) {
                        if (hasFirst && fml.express != null) {
                            if (fml.express == StampFormula.Express.ADD) sb.append(" + ");
                            if (fml.express == StampFormula.Express.MINUS) sb.append(" - ");
                        }
                        if (fml.column != null) {
                            sb.append(this.reference.getColumnName(wrapper, update, fml.column));
                        } else {
                            if (fml.value instanceof Number) {
                                Number fmlValue = (Number) fml.value;
                                if (fml.value instanceof Float) sb.append(fmlValue.floatValue());
                                else if (fml.value instanceof Double) sb.append(fmlValue.doubleValue());
                                else if (fml.value instanceof Integer) sb.append(fmlValue.intValue());
                                else if (fml.value instanceof Long) sb.append(fmlValue.longValue());
                                else sb.append(fmlValue.intValue());
                            } else if (fml.value instanceof String) {
                                sb.append(fml.value);
                            }
                        }
                        hasFirst = true;
                    }
                }
            }
        } else {
            sb.append("?");
            placeholders.add(new SQLDataPlaceholder(name, item.value));
        }
    }
}
