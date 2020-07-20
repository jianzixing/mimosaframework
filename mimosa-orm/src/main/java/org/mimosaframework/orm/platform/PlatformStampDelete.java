package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampDelete;
import org.mimosaframework.orm.sql.stamp.StampFrom;

import java.util.ArrayList;
import java.util.List;

public abstract class PlatformStampDelete extends PlatformStampCommonality {
    public PlatformStampDelete(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampDelete delete = (StampDelete) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");

        this.buildDeleteFrom(wrapper, delete, sb);
        this.buildDeleteWhere(wrapper, delete, sb, placeholders);

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    protected void buildDeleteFrom(MappingGlobalWrapper wrapper,
                                   StampDelete delete,
                                   StringBuilder sb) {
        StampFrom from = delete.from;
        sb.append(this.reference.getTableName(wrapper, from.table, from.name));
        if (StringTools.isNotEmpty(from.aliasName)) {
            sb.append(" AS " + from.aliasName);
        }
    }

    protected void buildDeleteWhere(MappingGlobalWrapper wrapper,
                                    StampDelete delete,
                                    StringBuilder sb,
                                    List<SQLDataPlaceholder> placeholders) {
        if (delete.where != null) {
            sb.append(" WHERE ");
            this.share.buildWhere(wrapper, placeholders, delete, delete.where, sb);
        }
    }
}
