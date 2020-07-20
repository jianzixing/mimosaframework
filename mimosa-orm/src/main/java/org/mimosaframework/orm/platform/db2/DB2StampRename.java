package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyRenameType;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampRename;

public class DB2StampRename extends PlatformStampRename {

    public DB2StampRename(PlatformStampSection section,
                          PlatformStampReference reference,
                          PlatformDialect dialect,
                          PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampRename stampRename = (StampRename) action;
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE");
        String tableName = this.reference.getTableName(wrapper, stampRename.tableClass, stampRename.tableName);
        sb.append(" " + tableName);
        sb.append(" RENAME");
        if (stampRename.renameType == KeyRenameType.COLUMN) {
            sb.append(" COLUMN");
            sb.append(" " + this.reference.getColumnName(wrapper, stampRename, new StampColumn(stampRename.oldName)));
            sb.append(" TO");
            sb.append(" " + this.reference.getColumnName(wrapper, stampRename, new StampColumn(stampRename.newName)));
        }
        if (stampRename.renameType == KeyRenameType.INDEX) {
            sb.append(" INDEX");
            sb.append(" " + stampRename.oldName);
            sb.append(" TO");
            sb.append(" " + stampRename.newName);
        }
        if (stampRename.renameType == KeyRenameType.TABLE) {
            sb.setLength(0);
            sb.append("RENAME TABLE");
            sb.append(" " + tableName + " TO");
            if (StringTools.isNotEmpty(stampRename.newName)) {
                sb.append(" " + stampRename.newName.toUpperCase());
            }
        }

        return new SQLBuilderCombine(sb.toString(), null);
    }
}
