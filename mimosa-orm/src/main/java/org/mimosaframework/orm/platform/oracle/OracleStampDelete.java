package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampDelete;
import org.mimosaframework.orm.sql.stamp.StampFrom;

import java.util.ArrayList;
import java.util.List;

public class OracleStampDelete extends PlatformStampDelete {

    public OracleStampDelete(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampDelete delete = (StampDelete) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");

        StampFrom from = delete.from;
        sb.append(this.reference.getTableName(wrapper, from.table, from.name));
        if (StringTools.isNotEmpty(from.aliasName)) {
            sb.append(" AS " + from.aliasName);
        }

        if (delete.where != null) {
            sb.append(" WHERE ");
            this.share.buildWhere(wrapper, placeholders, delete, delete.where, sb);
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }
}
