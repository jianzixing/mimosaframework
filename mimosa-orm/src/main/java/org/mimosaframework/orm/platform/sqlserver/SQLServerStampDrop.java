package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyTarget;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampDrop;

public class SQLServerStampDrop extends PlatformStampDrop {
    public SQLServerStampDrop(PlatformStampSection section,
                              PlatformStampReference reference,
                              PlatformDialect dialect,
                              PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampDrop drop = (StampDrop) action;
        StringBuilder sb = new StringBuilder();
        sb.append("DROP");
        if (drop.target == KeyTarget.DATABASE) {
            sb.append(" DATABASE");
            if (drop.checkExist) {
                sb.append(" IF EXIST");
            }
            sb.append(" " + drop.databaseName);
        }
        if (drop.target == KeyTarget.TABLE) {
            sb.append(" TABLE");
            if (drop.checkExist) {
                sb.append(" IF EXIST");
            }
            sb.append(" " + this.reference.getTableName(wrapper, drop.tableClass, drop.tableName));
        }
        if (drop.target == KeyTarget.INDEX) {
            sb.append(" INDEX");
            sb.append(" " + drop.indexName);
            sb.append(" ON");
            sb.append(" " + this.reference.getTableName(wrapper, drop.tableClass, drop.tableName));
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
