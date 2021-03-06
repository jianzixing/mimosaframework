package org.mimosaframework.orm.platform.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyTarget;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampDrop;

public class DB2StampDrop extends PlatformStampDrop {
    private static final Log logger = LogFactory.getLog(DB2StampDrop.class);

    public DB2StampDrop(PlatformStampSection section,
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
            sb = null;
            logger.warn("db2 can't drop database in current operation");
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
            sb.append(" " + this.reference.getWrapStart() + drop.indexName + this.reference.getWrapEnd());
        }
        return new SQLBuilderCombine(sb != null ? sb.toString() : null, null);
    }
}
