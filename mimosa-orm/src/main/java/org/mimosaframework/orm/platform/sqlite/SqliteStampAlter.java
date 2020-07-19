package org.mimosaframework.orm.platform.sqlite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

public class SqliteStampAlter extends PlatformStampAlter {
    private static final Log logger = LogFactory.getLog(SqliteStampAlter.class);

    public SqliteStampAlter(PlatformStampSection section,
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
        if (alter.target == KeyTarget.DATABASE) {
            // sb.append(" DATABASE");
            // sb.append(" " + RS + alter.name + RE);
            sb.setLength(0);

            if (StringTools.isNotEmpty(alter.charset)) {
                logger.warn("sqlite can't set database charset");
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            if (alter.items != null) {
                for (StampAlterItem item : alter.items) {
                    this.buildAlterItem(wrapper, sb, alter, item);
                }
            }
        }
        return new SQLBuilderCombine(sb.toString(), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        String tableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName);
        if (item.action == KeyAction.ADD) {

            if (item.struct == KeyAlterStruct.COLUMN) {
                sb.append("ALTER");
                sb.append(" TABLE");

                sb.append(" " + tableName);

                sb.append(" ADD");
                this.buildAlterColumn(sb, wrapper, alter, item);
            }
            if (item.struct == KeyAlterStruct.PRIMARY_KEY) {
                this.buildAddPrimaryKey(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.MODIFY) {
            sb.setLength(0);
            logger.warn("sqlite can't modify column");
            return;
        }

        if (item.action == KeyAction.DROP) {
            if (item.dropType == KeyAlterDropType.COLUMN) {
                sb.setLength(0);
                logger.warn("sqlite can't drop column");
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.setLength(0);
                logger.warn("sqlite can't drop primary key");
            }
        }

        if (item.action == KeyAction.AUTO_INCREMENT) {
            logger.warn("sqlite can't set auto_increment value");
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            logger.warn("sqlite can't set table charset");
        }
        if (item.action == KeyAction.COMMENT) {
            logger.warn("sqlite can't set table comment");
        }
    }

    private void buildAddPrimaryKey(StringBuilder sb,
                                    MappingGlobalWrapper wrapper,
                                    StampAlter alter,
                                    StampAlterItem item) {
        sb.setLength(0);
        logger.warn("sqlite can't create primary key");

        if (StringTools.isNotEmpty(item.comment)) {
            logger.warn("sqlite can't set index comment");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column) {
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
            if (column.defaultValue.equals("*****")) {
                sb.append(" DEFAULT NULL");
            } else {
                sb.append(" DEFAULT '" + column.defaultValue + "'");
            }
        }
        if (StringTools.isNotEmpty(column.comment)) {
            logger.warn("sqlite can't set column comment");
        }
        if (column.after != null) {
            logger.warn("sqlite can't set column order");
        }
        if (column.before != null) {
            logger.warn("sqlite can't set column order");
        }
    }
}
