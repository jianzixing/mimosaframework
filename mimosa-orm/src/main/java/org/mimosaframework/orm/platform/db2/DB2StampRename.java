package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class DB2StampRename extends DB2StampCommonality implements StampCombineBuilder {

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampRename stampRename = (StampRename) action;
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE");
        String tableName = this.getTableName(wrapper, stampRename.tableClass, stampRename.tableName);
        sb.append(" " + tableName);
        sb.append(" RENAME");
        if (stampRename.renameType == KeyRenameType.COLUMN) {
            sb.append(" COLUMN");
            sb.append(" " + this.getColumnName(wrapper, stampRename, new StampColumn(stampRename.oldName)));
            sb.append(" TO");
            sb.append(" " + this.getColumnName(wrapper, stampRename, new StampColumn(stampRename.newName)));
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
