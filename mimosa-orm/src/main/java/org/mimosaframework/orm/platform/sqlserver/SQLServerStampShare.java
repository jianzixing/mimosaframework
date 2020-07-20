package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampShare;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampAlter;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampCreate;

public class SQLServerStampShare extends PlatformStampShare {
    protected boolean isDeclareCheckComment = false;
    protected boolean isDeclareCheckTableComment = false;

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type,
                              boolean isCheckHasTable) {
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
        String tableName = this.commonality.getReference().getTableName(wrapper, table, tableStr, false);

        if (type == 1) {
            StampColumn column = (StampColumn) param;
            if (table != null) {
                column.table = table;
            } else if (StringTools.isNotEmpty(tableStr)) {
                column.tableAliasName = tableStr;
            }
            String columnName = this.commonality.getReference().getColumnName(wrapper, action, new StampColumn(column.column), false);
            if (!isDeclareCheckComment) {
                this.commonality.getSection().getDeclares().add("@EXIST_COLUMN_COMMENT INT");
                this.isDeclareCheckComment = true;
            }
            this.commonality.getSection().getBuilders().add(new ExecuteImmediate()
                    .setProcedure("SELECT @EXIST_COLUMN_COMMENT=(SELECT COUNT(1) FROM SYS.COLUMNS A " +
                            "LEFT JOIN SYS.EXTENDED_PROPERTIES G ON (A.OBJECT_ID = G.MAJOR_ID AND G.MINOR_ID = A.COLUMN_ID) " +
                            "WHERE OBJECT_ID = (SELECT OBJECT_ID FROM SYS.TABLES WHERE NAME = '" + tableName + "') " +
                            "AND A.NAME='" + columnName + "' AND G.VALUE IS NOT NULL)"));
            this.commonality.getSection().getBuilders().add(new ExecuteImmediate().setProcedure(
                    (isCheckHasTable ? "IF (@HAS_TABLE = 0) BEGIN " + this.commonality.getSection().getNTab() : "") +
                            "IF (@EXIST_COLUMN_COMMENT = 1) " +
                            "EXEC SP_UPDATEEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "', 'COLUMN', '" + columnName + "';" +
                            this.commonality.getSection().getNTab() + "ELSE " +
                            "EXEC SP_ADDEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "', 'COLUMN', '" + columnName + "';" +
                            (isCheckHasTable ? this.commonality.getSection().getNTab() + "END" : "")
            ));
        }
        if (type == 2) {
            if (!isDeclareCheckTableComment) {
                this.commonality.getSection().getDeclares().add("@EXIST_TABLE_COMMENT INT");
                this.isDeclareCheckTableComment = true;
            }
            this.commonality.getSection().getBuilders().add(new ExecuteImmediate()
                    .setProcedure("SELECT @EXIST_TABLE_COMMENT=(SELECT COUNT(DISTINCT B.NAME) " +
                            "FROM SYS.SYSCOLUMNS A " +
                            "INNER JOIN SYS.SYSOBJECTS B ON A.ID = B.ID " +
                            "LEFT JOIN SYS.SYSCOMMENTS C ON A.CDEFAULT = C.ID " +
                            "LEFT JOIN SYS.EXTENDED_PROPERTIES F ON B.ID = F.MAJOR_ID AND F.MINOR_ID = 0 " +
                            "WHERE B.NAME='" + tableName + "' AND F.VALUE IS NOT NULL)"));
            this.commonality.getSection().getBuilders().add(new ExecuteImmediate().setProcedure(
                    (isCheckHasTable ? "IF (@HAS_TABLE = 0) BEGIN " + this.commonality.getSection().getNTab() : "") +
                            "IF (@EXIST_TABLE_COMMENT = 1) " +
                            "EXEC SP_UPDATEEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "'" +
                            this.commonality.getSection().getNTab() + "ELSE " +
                            "EXEC SP_ADDEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "'" +
                            (isCheckHasTable ? this.commonality.getSection().getNTab() + "END" : "")
            ));
        }
    }
}
