package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampShare;
import org.mimosaframework.orm.sql.stamp.*;

public class DB2StampShare extends PlatformStampShare {
    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type) {
        Class table = null;
        String tableStr = null;
        if (action instanceof StampAlter) {
            table = ((StampAlter) action).tableClass;
            tableStr = ((StampAlter) action).tableName;
        }
        if (action instanceof StampCreate) {
            table = ((StampCreate) action).tableClass;
            tableStr = ((StampCreate) action).tableName;
        }

        StringBuilder comment = new StringBuilder();
        if (type == 1) {
            StampColumn column = (StampColumn) param;
            comment.append("COMMENT ON COLUMN ");
            if (table != null) {
                column.table = table;
            } else if (StringTools.isNotEmpty(tableStr)) {
                column.tableAliasName = tableStr;
            }
            comment.append(this.commonality.getReference().getColumnName(wrapper, action, column));
        }
        if (type == 2) {
            String tableName = this.commonality.getReference().getTableName(wrapper, table, tableStr);
            comment.append("COMMENT ON TABLE " + tableName);
        }
        comment.append(" IS ");
        comment.append("''" + commentStr + "''");
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate(comment));
    }
}
