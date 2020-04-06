package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class OracleStampAlter extends OracleStampCommonality implements StampCombineBuilder {
    private static final Log logger = LogFactory.getLog(OracleStampAlter.class);
    protected int totalAction = 0;
    protected boolean noNeedSource = false;

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
                sb.append(" CHARACTER SET " + alter.charset);
            }
            if (StringTools.isNotEmpty(alter.collate)) {
                sb.append(" COLLATE " + alter.collate);
            }
        }
        if (alter.target == KeyTarget.TABLE) {
            sb.append(" TABLE");

            sb.append(" " + this.getTableName(wrapper, alter.table, alter.name));

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
        return new SQLBuilderCombine(this.toSQLString(new ExecuteImmediate(sb)), null);
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
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.CHANGE) {
            totalAction++;
            sb.append(" MODIFY");
            String oldColumnName = this.getColumnName(wrapper, alter, item.oldColumn);
            String newColumnName = this.getColumnName(wrapper, alter, item.column);
            if (!oldColumnName.equalsIgnoreCase(newColumnName)) {
                StringBuilder rnsb = new StringBuilder();
                rnsb.append("ALTER TABLE");
                rnsb.append(" " + this.getTableName(wrapper, alter.table, alter.name));
                rnsb.append(" RENAME COLUMN " + oldColumnName + " TO " + newColumnName);
                this.getBuilders().add(new ExecuteImmediate(rnsb));
            }
            this.buildAlterColumn(sb, wrapper, alter, item, true);
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
                sb.append(" " + this.getColumnName(wrapper, alter, item.column));
            }
            if (item.dropType == KeyAlterDropType.INDEX) {
                sb.setLength(0);
                sb.append("DROP");
                sb.append(" INDEX");
                sb.append(" " + RS + item.name + RE);
            }
            if (item.dropType == KeyAlterDropType.PRIMARY_KEY) {
                sb.append(" PRIMARY KEY");
            }
        }

        if (item.action == KeyAction.RENAME) {
            totalAction++;
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
            totalAction++;
            this.noNeedSource = true;
            String tableName = this.getTableName(wrapper, alter.table, alter.name);
            String seqName = tableName + "_SEQ";

            this.getDeclares().add("CACHE_CUR_SEQ NUMBER");
            this.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT " + seqName + ".NEXTVAL INTO CACHE_CUR_SEQ FROM DUAL"));
            this.getBuilders().add(new ExecuteImmediate().setProcedure("EXECUTE IMMEDIATE concat('ALTER SEQUENCE " +
                    seqName + " INCREMENT BY '," + item.value + "-CACHE_CUR_SEQ)"));
            this.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT " + seqName + ".NEXTVAL INTO CACHE_CUR_SEQ FROM DUAL"));
            this.getBuilders().add(new ExecuteImmediate("ALTER SEQUENCE " + seqName + " INCREMENT BY 1"));
        }
        if (item.action == KeyAction.CHARACTER_SET) {
            totalAction++;
            sb.append(" CHARACTER SET = " + item.name);
        }
        if (item.action == KeyAction.COMMENT) {
            totalAction++;
            this.addCommentSQL(wrapper, alter, item, item.comment, 2);
        }
    }

    private void buildAlterIndex(StringBuilder sb,
                                 MappingGlobalWrapper wrapper,
                                 StampAlter alter,
                                 StampAlterItem item) {
        if (item.indexType != KeyIndexType.PRIMARY_KEY) {
            sb.setLength(0);
            sb.append("CREATE");
        }
        if (item.indexType == KeyIndexType.UNIQUE) {
            sb.append(" UNIQUE");
            sb.append(" INDEX");
        } else if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            sb.append(" PRIMARY KEY");
        } else {
            sb.append(" INDEX");
        }

        if (StringTools.isNotEmpty(item.name)) {
            sb.append(" " + RS + item.name + RE);
        }
        if (item.indexType != KeyIndexType.PRIMARY_KEY) {
            sb.append(" ON ");
            sb.append(this.getTableName(wrapper, alter.table, alter.name));
        }

        List<String> fullTextIndexNames = new ArrayList<>();

        if (item.columns != null && item.columns.length > 0) {
            for (StampColumn column : item.columns) {
                fullTextIndexNames.add(this.getColumnName(wrapper, alter, column, false));
            }
            sb.append(" (");
            int i = 0;
            for (StampColumn column : item.columns) {
                sb.append(this.getColumnName(wrapper, alter, column));
                i++;
                if (i != item.columns.length) {
                    sb.append(",");
                }
            }
            sb.append(")");
        } else {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    StampAction.class, "miss_index_columns"));
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
        sb.append(" " + this.getColumnName(wrapper, alter, isOldColumn ? column.oldColumn : column.column));
        if (column.columnType != null) {
            sb.append(" " + this.getColumnType(column.columnType, column.len, column.scale));
        }
        if (!column.nullable) {
            sb.append(" NOT NULL");
        }
        if (column.autoIncrement) {
            this.addAutoIncrement(wrapper, alter.table, alter.name);
        }
        if (column.pk) {
            sb.append(" PRIMARY KEY");
        }
        if (column.unique) {
            sb.append(" UNIQUE");
        }
        if (column.key) {
            sb.append(" KEY");
        }
        if (StringTools.isNotEmpty(column.defaultValue)) {
            sb.append(" DEFAULT \"" + column.defaultValue + "\"");
        }
        if (StringTools.isNotEmpty(column.comment)) {
            this.addCommentSQL(wrapper, alter, column.column, column.comment, 1);
        }
        if (column.after != null) {
            sb.append(" AFTER " + this.getColumnName(wrapper, alter, column.after));
        }
        if (column.before != null) {
            sb.append(" BEFORE " + this.getColumnName(wrapper, alter, column.before));
        }
    }
}
