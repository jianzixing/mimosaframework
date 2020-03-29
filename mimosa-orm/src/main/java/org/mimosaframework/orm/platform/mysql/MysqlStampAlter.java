package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

public class MysqlStampAlter extends MysqlAbstractStamp implements StampCombineBuilder {

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper,
                                           StampAction action) {
        StampAlter alter = (StampAlter) action;
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER");
        if (alter.target == KeyTarget.DATABASE) {
            sb.append(" DATABASE");

            sb.append(" " + RS + alter.name + RE);

            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append(" CHARSET " + alter.charset);
            }
            if (StringTools.isNotEmpty(alter.collate)) {
                sb.append(" COLLATE " + alter.collate);
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            sb.append(" TABLE");

            sb.append(" " + this.getTableName(wrapper, alter.table, alter.name));

            for (StampAlterItem item : alter.items) {
                this.buildAlterItem(wrapper, sb, alter, item);
            }

        }
        return null;
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        if (item.action == KeyAction.ADD) {
            sb.append(" ADD");
            if (item.struct == KeyAlterStruct.COLUMN) {
                this.buildAlterColumn(sb, wrapper, alter, item);
            }
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.CHANGE) {
            sb.append(" CHANGE");
            sb.append(" " + this.getColumnName(wrapper, alter, item.oldColumn));
            this.buildAlterColumn(sb, wrapper, alter, item);
        }

        if (item.action == KeyAction.MODIFY) {
            sb.append(" MODIFY");
            this.buildAlterColumn(sb, wrapper, alter, item);
        }

        if (item.action == KeyAction.DROP) {
            sb.append(" DROP");
            if (item.dropType == KeyAlterDropType.COLUMN) {
                sb.append(" COLUMN");
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.INDEX) {
                sb.append(" INDEX");
                sb.append(" " + item.name);
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.append(" PRIMARY KEY");
            }
        }

        if (item.action == KeyAction.RENAME) {
            sb.append(" RENAME");
            if (item.renameType == KeyAlterRenameType.COLUMN) {
                sb.append(" COLUMN");
                sb.append(" " + this.getColumnName(wrapper, alter, item.oldColumn));
                sb.append(" TO");
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.renameType == KeyAlterRenameType.INDEX) {
                sb.append(" INDEX");
                sb.append(" " + item.oldName);
                sb.append(" TO");
                sb.append(" " + item.name);
            }
            if (item.renameType == KeyAlterRenameType.TABLE) {
                sb.append(" " + item.name);
            }
        }

        if (item.action == KeyAction.AUTO_INCREMENT) {
            sb.append(" AUTO_INCREMENT = " + item.value);
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            sb.append(" CHARACTER SET = " + item.name);
        }
        if (item.action == KeyAction.COMMENT) {
            sb.append(" COMMENT = \"" + item.comment + "\"");
        }
    }

    private void buildAlterIndex(StringBuilder sb,
                                 MappingGlobalWrapper wrapper,
                                 StampAlter alter,
                                 StampAlterItem item) {
        sb.append(" INDEX");
        sb.append(" " + item.name);
        sb.append(" (");
        int i = 0;
        for (StampColumn column : item.columns) {
            sb.append(this.getColumnName(wrapper, alter, column));
            i++;
            if (i != item.columns.length) sb.append(",");
        }
        sb.append(")");

        if (StringTools.isNotEmpty(item.comment)) {
            sb.append(" COMMENT \"" + item.comment + "\"");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem item) {
        sb.append(" " + this.getColumnName(wrapper, alter, item.column));
        if (item.columnType != null) {
            sb.append(" " + item.columnType);
        }
        if (!item.nullable) {
            sb.append(" NOT NULL");
        }
        if (!item.autoIncrement) {
            sb.append(" AUTO_INCREMENT");
        }
        if (!item.pk) {
            sb.append(" PRIMARY KEY");
        }
        if (!item.unique) {
            sb.append(" UNIQUE");
        }
        if (!item.key) {
            sb.append(" KEY");
        }
        if (StringTools.isNotEmpty(item.defaultValue)) {
            sb.append(" DEFAULT \"" + item.defaultValue + "\"");
        }
        if (StringTools.isNotEmpty(item.comment)) {
            sb.append(" COMMENT \"" + item.comment + "\"");
        }
        if (item.after != null) {
            sb.append(" AFTER " + this.getColumnName(wrapper, alter, item.after));
        }
        if (item.before != null) {
            sb.append(" BEFORE " + this.getColumnName(wrapper, alter, item.before));
        }
    }
}
