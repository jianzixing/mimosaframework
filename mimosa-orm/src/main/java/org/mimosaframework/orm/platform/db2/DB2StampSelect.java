package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class DB2StampSelect extends PlatformStampSelect {
    public DB2StampSelect(PlatformStampSection section,
                          PlatformStampReference reference,
                          PlatformDialect dialect,
                          PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampSelect select = (StampSelect) action;
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();

        this.buildSelect(wrapper, select, sb, placeholders);
        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    public void buildSelect(MappingGlobalWrapper wrapper,
                            StampSelect select,
                            StringBuilder sb,
                            List<SQLDataPlaceholder> placeholders) {
        StringBuilder orderBySql = new StringBuilder();
        this.buildSelectOrderBy(wrapper, select, orderBySql);

        if (select.limit != null) {
            sb.append("SELECT * FROM (");
            if (orderBySql != null) {
                sb.append("SELECT *, ROW_NUMBER() OVER (" + orderBySql + ") AS RN_ALIAS_ROW_NUMBER FROM (");
            } else {
                sb.append("SELECT *, ROW_NUMBER() OVER () AS RN_ALIAS_ROW_NUMBER FROM (");
            }
        }

        sb.append("SELECT ");
        this.buildFields(wrapper, select, sb, false);

        sb.append(" FROM ");
        this.buildSelectForms(wrapper, select, sb, placeholders);
        this.buildSelectJoins(wrapper, select, sb, placeholders);
        this.buildSelectWhere(wrapper, select, sb, placeholders);
        this.buildSelectGroupBy(wrapper, select, sb);
        this.buildSelectHaving(wrapper, select, sb, placeholders);

        if (select.orderBy != null && select.orderBy.length > 0 && select.limit == null) {
            sb.append(orderBySql);
        }

        if (select.limit != null) {
            long start = select.limit.start, limit = start + select.limit.limit;
            sb.append(") RN_TABLE_ALIAS ) ");
            sb.append("WHERE RN_ALIAS_ROW_NUMBER BETWEEN " + start + " AND " + limit);
        }

        if (select.forUpdate) {
            sb.append(" FOR UPDATE");
        }
    }
}
