package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

public class OracleStampAlter extends PlatformStampAlter {
    private static final Log logger = LogFactory.getLog(OracleStampAlter.class);
    protected int totalAction = 0;
    protected boolean noNeedSource = false;

    public OracleStampAlter(PlatformStampSection section,
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
                sb.append(" CHARACTER SET " + alter.charset);
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
            } else {
                // oracle 没有修改表字符集的设置
                sb = null;
                logger.warn("oracle can't set table charset");
            }
        }

        if (totalAction <= 1 && noNeedSource) sb = null;
        if (sb != null) {
            String sql = sb.toString();
            if (StringTools.isNotEmpty(sql) && this.section.multiExecuteImmediate()) {
                sql = sql.replaceAll("'", "''");
            }
            return new SQLBuilderCombine(this.section.toSQLString(new ExecuteImmediate(sql)), null);
        }
        return new SQLBuilderCombine(this.section.toSQLString(new ExecuteImmediate(sb)), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        if (item.action == KeyAction.ADD) {
            totalAction++;
            sb.append(" ADD");
            if (item.struct == KeyAlterStruct.COLUMN) {
                this.buildAlterColumn(sb, wrapper, alter, item, false);
            }
            if (item.struct == KeyAlterStruct.PRIMARY_KEY) {
                this.buildAddPrimaryKey(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.MODIFY) {
            totalAction++;
            sb.append(" MODIFY");
            this.buildAlterColumn(sb, wrapper, alter, item, false);
        }

        if (item.action == KeyAction.DROP) {
            totalAction++;
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
            totalAction++;
            this.noNeedSource = true;
            String tableName = this.reference.getTableName(wrapper, alter.tableClass, alter.tableName);
            String seqName = tableName + "_SEQ";

            this.section.getDeclares().add("CACHE_CUR_SEQ NUMBER");
            this.section.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT " + seqName + ".NEXTVAL INTO CACHE_CUR_SEQ FROM DUAL"));
            this.section.getBuilders().add(new ExecuteImmediate().setProcedure("EXECUTE IMMEDIATE concat('ALTER SEQUENCE " +
                    seqName + " INCREMENT BY '," + item.value + "-CACHE_CUR_SEQ)"));
            this.section.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT " + seqName + ".NEXTVAL INTO CACHE_CUR_SEQ FROM DUAL"));
            this.section.getBuilders().add(new ExecuteImmediate("ALTER SEQUENCE " + seqName + " INCREMENT BY 1"));
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            totalAction++;
            sb.append(" CHARACTER SET = " + item.charset);
        }
        if (item.action == KeyAction.COMMENT) {
            totalAction++;
            this.share.addCommentSQL(wrapper, alter, item, item.comment, 2);
        }
    }

    private void buildAddPrimaryKey(StringBuilder sb,
                                    MappingGlobalWrapper wrapper,
                                    StampAlter alter,
                                    StampAlterItem item) {
        sb.append(" PRIMARY KEY");

        if (StringTools.isNotEmpty(item.indexName)) {
            sb.append(" " + this.reference.getWrapStart() + item.indexName + this.reference.getWrapEnd());
        }

        if (item.columns != null && item.columns.length > 0) {
            sb.append(" (");
            int i = 0;
            for (StampColumn column : item.columns) {
                sb.append(this.reference.getColumnName(wrapper, alter, column));
                i++;
                if (i != item.columns.length) {
                    sb.append(",");
                }
            }
            sb.append(")");
        } else {
            throw new IllegalArgumentException(I18n.print("miss_index_columns"));
        }

        // oracle 没有所以注释 common on
        if (StringTools.isNotEmpty(item.comment)) {
            logger.warn("oracle can't set index comment");
        }
    }

    private void buildAlterColumn(StringBuilder sb,
                                  MappingGlobalWrapper wrapper,
                                  StampAlter alter,
                                  StampAlterItem column,
                                  boolean isOldColumn) {
        sb.append(" " + this.reference.getColumnName(wrapper, alter, isOldColumn ? column.oldColumn : column.column));
        if (column.columnType != null) {
            sb.append(" " + this.share.getColumnType(column.columnType, column.len, column.scale));
        }
        if (column.autoIncrement == KeyConfirm.YES) {
            this.share.addAutoIncrement(wrapper, alter.tableClass, alter.tableName);
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
        if (column.nullable == KeyConfirm.NO) {
            sb.append(" NOT NULL");
        }

        if (StringTools.isNotEmpty(column.comment)) {
            this.share.addCommentSQL(wrapper, alter, column.column, column.comment, 1);
        }
        if (column.after != null) {
            logger.warn("oracle can't set column order");
        }
        if (column.before != null) {
            logger.warn("oracle can't set column order");
        }
    }
}
