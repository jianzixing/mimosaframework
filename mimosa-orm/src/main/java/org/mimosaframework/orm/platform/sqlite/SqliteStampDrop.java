package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyTarget;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampDrop;

public class SqliteStampDrop extends PlatformStampDrop {
    private static final Log logger = LogFactory.getLog(SqliteStampDrop.class);

    public SqliteStampDrop(PlatformStampSection section,
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
            sb.setLength(0);
            logger.warn("sqlite drop database");
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
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
