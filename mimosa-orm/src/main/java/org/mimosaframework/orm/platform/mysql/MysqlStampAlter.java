package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

public class MysqlStampAlter extends PlatformStampAlter {

    public MysqlStampAlter(PlatformStampSection section,
                           PlatformStampReference reference,
                           PlatformDialect dialect,
                           PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper,
                                           StampAction action) {
        StampAlter alter = (StampAlter) action;
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER");
        if (alter.target == KeyTarget.DATABASE) {
            sb.append(" DATABASE");

            sb.append(" " + this.reference.getWrapStart() + alter.databaseName + this.reference.getWrapEnd());

            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append(" CHARSET " + alter.charset);
            }
            if (StringTools.isNotEmpty(alter.collate)) {
                sb.append(" COLLATE " + alter.collate);
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            sb.append(" TABLE");

            sb.append(" " + this.reference.getTableName(wrapper, alter.tableClass, alter.tableName));

            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
            if (StringTools.isNotEmpty(alter.charset)) {
                sb.append(" CHARSET " + alter.charset);
            }
            if (StringTools.isNotEmpty(alter.collate)) {
                sb.append(" COLLATE " + alter.collate);
            }
        }
        return new SQLBuilderCombine(sb.toString(), null);
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
            if (item.struct == KeyAlterStruct.PRIMARY_KEY) {
                this.buildAddPrimaryKey(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.MODIFY) {
            sb.append(" MODIFY");
            this.buildAlterColumn(sb, wrapper, alter, item);
        }

        if (item.action == KeyAction.DROP) {
            sb.append(" DROP");
            if (item.dropType == KeyAlterDropType.COLUMN) {
                sb.append(" COLUMN");
                sb.append(" " + this.reference.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.append(" PRIMARY KEY");
            }
        }

        if (item.action == KeyAction.AUTO_INCREMENT) {
            sb.append(" AUTO_INCREMENT = " + item.value);
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            sb.append(" CHARACTER SET = " + item.charset);
        }
        if (item.action == KeyAction.COMMENT) {
            sb.append(" COMMENT = \"" + item.comment + "\"");
        }
    }

    private void buildAddPrimaryKey(StringBuilder sb,
                                    MappingGlobalWrapper wrapper,
                                    StampAlter alter,
                                    StampAlterItem item) {
        sb.append(" PRIMARY KEY");
        if (StringTools.isNotEmpty(item.indexName)) {
            sb.append(" " + item.indexName);
        }

        if (item.columns != null) {
            sb.append(" (");
            int i = 0;
            for (StampColumn column : item.columns) {
                sb.append(this.reference.getColumnName(wrapper, alter, column));
                i++;
                if (i != item.columns.length) sb.append(",");
            }
            sb.append(")");
        } else {
            throw new IllegalArgumentException(I18n.print("miss_index_columns"));
        }

        if (StringTools.isNotEmpty(item.comment)) {
            sb.append(" COMMENT \"" + item.comment + "\"");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column) {
        if (column.timeForUpdate) {
            sb.append(" " + this.reference.getColumnName(wrapper, alter, column.column));
            sb.append(" TIMESTAMP");
            sb.append(" NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        } else {
            sb.append(" " + this.reference.getColumnName(wrapper, alter, column.column));
            if (column.columnType != null) {
                sb.append(" " + this.share.getColumnType(column.columnType, column.len, column.scale));
            }
            if (column.nullable == KeyConfirm.NO) {
                sb.append(" NOT NULL");
            }
            if (column.autoIncrement == KeyConfirm.YES) {
                sb.append(" AUTO_INCREMENT");
            }
            if (column.pk == KeyConfirm.YES) {
                sb.append(" PRIMARY KEY");
            }
            if (column.defaultValue != null) {
                sb.append(" DEFAULT \"" + column.defaultValue + "\"");
            }
        }
        if (StringTools.isNotEmpty(column.comment)) {
            sb.append(" COMMENT \"" + column.comment + "\"");
        }
        if (column.after != null) {
            sb.append(" AFTER " + this.reference.getColumnName(wrapper, alter, column.after));
        } else if (column.first) {
            sb.append(" FIRST");
        }
    }
}
