package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class PostgreSQLStampSelect extends PlatformStampSelect {
    public PostgreSQLStampSelect(PlatformStampSection section,
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
        sb.append("SELECT ");
        this.buildFields(wrapper, select, sb, false);

        sb.append(" FROM ");
        this.buildSelectForms(wrapper, select, sb, placeholders);
        this.buildSelectJoins(wrapper, select, sb, placeholders);
        this.buildSelectWhere(wrapper, select, sb, placeholders);
        this.buildSelectGroupBy(wrapper, select, sb);
        this.buildSelectHaving(wrapper, select, sb, placeholders);
        this.buildSelectOrderBy(wrapper, select, sb);

        if (select.limit != null) {
            sb.append(" LIMIT " + select.limit.limit + " OFFSET " + select.limit.start);
        }

        if (select.forUpdate) {
            sb.append(" FOR UPDATE");
        }
    }
}
