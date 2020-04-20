package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class MysqlStampRename extends MysqlStampCommonality implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampRename stampRename = (StampRename) action;
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER");
        sb.append(" TABLE");
        sb.append(" " + this.getTableName(wrapper, stampRename.tableClass, stampRename.tableName));
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
            sb.append(" " + stampRename.newName);
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }
}
