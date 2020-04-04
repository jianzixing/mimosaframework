package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.List;

public class OracleStampAlter extends OracleStampCommonality implements StampCombineBuilder {

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
        return new SQLBuilderCombine(this.toSQLString(sb), null);
    }

    private void buildAlterItem(MappingGlobalWrapper wrapper,
                                StringBuilder sb,
                                StampAlter alter,
                                StampAlterItem item) {
        if (item.action == KeyAction.ADD) {
            sb.append(" ADD");
            if (item.struct == KeyAlterStruct.COLUMN) {
                this.buildAlterColumn(sb, wrapper, alter, item, false);
            }
            if (item.struct == KeyAlterStruct.INDEX) {
                this.buildAlterIndex(sb, wrapper, alter, item);
            }
        }

        if (item.action == KeyAction.CHANGE) {
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
            sb.append(" MODIFY");
            this.buildAlterColumn(sb, wrapper, alter, item, false);
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
            this.addCommentSQL(wrapper, alter, item.column, item.comment);
        }
    }

    private void buildAlterIndex(StringBuilder sb,
                                 MappingGlobalWrapper wrapper,
                                 StampAlter alter,
                                 StampAlterItem item) {
        if (item.indexType == KeyIndexType.FULLTEXT) {
            sb.append(" FULLTEXT");
            sb.append(" INDEX");
        } else if (item.indexType == KeyIndexType.UNIQUE) {
            sb.append(" UNIQUE");
        } else if (item.indexType == KeyIndexType.PRIMARY_KEY) {
            sb.append(" PRIMARY KEY");
        } else {
            sb.append(" INDEX");
        }

        if (StringTools.isNotEmpty(item.name)) {
            sb.append(" " + item.name);
        }

        if (item.columns != null) {
            sb.append(" (");
            int i = 0;
            for (StampColumn column : item.columns) {
                sb.append(this.getColumnName(wrapper, alter, column));
                i++;
                if (i != item.columns.length) sb.append(",");
            }
            sb.append(")");
        } else {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    StampAction.class, "miss_index_columns"));
        }

        if (StringTools.isNotEmpty(item.comment)) {
            this.addCommentSQL(wrapper, alter, item.column, item.comment);
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
            this.addAutoIncrement(wrapper, alter);
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
            this.addCommentSQL(wrapper, alter, column.column, column.comment);
        }
        if (column.after != null) {
            sb.append(" AFTER " + this.getColumnName(wrapper, alter, column.after));
        }
        if (column.before != null) {
            sb.append(" BEFORE " + this.getColumnName(wrapper, alter, column.before));
        }
    }

    protected void addCommentSQL(MappingGlobalWrapper wrapper,
                                 StampAlter alter,
                                 StampColumn column,
                                 String commentStr) {
        List<StampAction.STItem> items = alter.getTables();
        if (items != null && items.size() > 0) {
            StringBuilder comment = new StringBuilder();
            comment.append("COMMENT ON COLUMN ");
            column.table = items.get(0).getTable();
            comment.append(this.getColumnName(wrapper, alter, column));
            comment.append(" IS ");
            comment.append("''" + commentStr + "''");
            this.getBuilders().add(new ExecuteImmediate(comment));
        }
    }

    protected void addAutoIncrement(MappingGlobalWrapper wrapper,
                                    StampAlter alter) {
        String tableName = this.getTableName(wrapper, alter.table, alter.name);
        String seqName = tableName + "_SEQ";
        this.getDeclares().add("SEQUENCE_COUNT NUMBER");
        this.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT 1 INTO SEQUENCE_COUNT FROM user_sequences WHERE sequence_name = '" + seqName + "'"));
        this.getBuilders().add(new ExecuteImmediate("IF (SEQUENCE_COUNT!=1) THEN ",
                "CREATE SEQUENCE " + seqName + " INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999999999999999", "END IF"));
    }
}
