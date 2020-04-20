package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class SqliteStampRename extends SqliteStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(SqliteStampRename.class);

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
            sb.append(" " + this.getTableName(wrapper, stampRename.tableClass, stampRename.tableName));
            sb.append(" RENAME");
            sb.append(" TO " + stampRename.newName);
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
