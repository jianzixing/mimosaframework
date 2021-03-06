package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.KeyRenameType;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampRename;

public class MysqlStampRename extends PlatformStampRename {
    public MysqlStampRename(PlatformStampSection section,
                            PlatformStampReference reference,
                            PlatformDialect dialect,
                            PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampRename stampRename = (StampRename) action;
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER");
        sb.append(" TABLE");
        sb.append(" " + this.reference.getTableName(wrapper, stampRename.tableClass, stampRename.tableName));
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
            sb.append(" " + stampRename.newName);
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
