package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyRenameType;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampRename;

public class SqliteStampRename extends PlatformStampRename {
    private static final Log logger = LogFactory.getLog(SqliteStampRename.class);

    public SqliteStampRename(PlatformStampSection section,
                             PlatformStampReference reference,
                             PlatformDialect dialect,
                             PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampRename stampRename = (StampRename) action;
        StringBuilder sb = new StringBuilder();
        if (stampRename.renameType == KeyRenameType.COLUMN) {
            logger.warn("sqlite can't modify column name");
        }
        if (stampRename.renameType == KeyRenameType.INDEX) {
            logger.warn("sqlite can't modify index name");
        }
        if (stampRename.renameType == KeyRenameType.TABLE) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + this.reference.getTableName(wrapper, stampRename.tableClass, stampRename.tableName));
            sb.append(" RENAME");
            sb.append(" TO " + stampRename.newName);
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
