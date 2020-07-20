package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyRenameType;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampRename;

public class SQLServerStampRename extends PlatformStampRename {
    public SQLServerStampRename(PlatformStampSection section,
                                PlatformStampReference reference,
                                PlatformDialect dialect,
                                PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampRename stampRename = (StampRename) action;
        StringBuilder sb = new StringBuilder();
        String tableName = this.reference.getTableName(wrapper, stampRename.tableClass, stampRename.tableName);
        if (stampRename.renameType == KeyRenameType.COLUMN) {
            String tableOutName = this.reference.getTableName(wrapper, stampRename.tableClass, stampRename.tableName, false);
            sb.append("EXEC SP_RENAME ");
            sb.append("\"" + tableOutName + "." + this.reference.getColumnName(wrapper, stampRename, new StampColumn(stampRename.oldName), false) + "\"");
            sb.append(",");
            sb.append(" \"" + this.reference.getColumnName(wrapper, stampRename, new StampColumn(stampRename.newName), false) + "\"");
            sb.append(", \"COLUMN\"");
        }
        if (stampRename.renameType == KeyRenameType.INDEX) {
            sb.append("ALTER");
            sb.append(" TABLE");
            sb.append(" " + tableName);
            sb.append(" RENAME");
            sb.append(" INDEX");
            sb.append(" " + stampRename.oldName);
            sb.append(" TO");
            sb.append(" " + stampRename.newName);
        }
        if (stampRename.renameType == KeyRenameType.TABLE) {
            sb.append(" EXEC sp_rename '" + this.reference.getTableName(wrapper, stampRename.tableClass, stampRename.tableName, false)
                    + "', '" + stampRename.newName + "'");
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
