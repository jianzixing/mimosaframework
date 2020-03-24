package org.mimosaframework.orm.sql.select;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public class AbstractSQLSelectBuilder
        extends
        AbstractSQLBuilder

        implements
        RedefineSelectBuilder {

    protected boolean hasLeastOneSort = false;

    @Override
    protected SQLBuilder createSQLBuilder() {
        return null;
    }

    @Override
    public MappingTable getMappingTableByClass(Class table) {
        return null;
    }

    private void setFieldItemA(FieldItem fieldItem) {
        Class table = fieldItem.getTable();
        String tableAliasName = fieldItem.getTableAliasName();
        Serializable field = fieldItem.getField();

        if (StringTools.isNotEmpty(tableAliasName)) {
            this.sqlBuilder.addTableWrapField(tableAliasName, field.toString());
        } else {
            if (table != null) {
                MappingTable mappingTable = this.getMappingTableByClass(table);
                this.sqlBuilder.addTableWrapField(mappingTable.getMappingTableName(), field.toString());
            }
        }
    }

    private void setFieldItemB(FieldItem fieldItem) {
        String fieldAliasName = fieldItem.getFieldAliasName();
        if (StringTools.isNotEmpty(fieldAliasName)) {
            this.sqlBuilder.AS().addWrapString(fieldAliasName);
        }
    }

    @Override
    public Object select() {
        this.sqlBuilder.SELECT();
        return this;
    }

    @Override
    public Object all() {
        this.sqlBuilder.addString("*");
        return this;
    }

    @Override
    public Object field(Serializable... fields) {
        if (fields != null) {
            int i = 0;
            for (Serializable field : fields) {
                this.sqlBuilder.addMappingField(new SQLMappingField(field));
                i++;
                if (i != fields.length) this.sqlBuilder.addSplit();
            }
        }
        return this;
    }

    @Override
    public Object field(Class table, Serializable... fields) {
        if (table != null && fields != null) {
            this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
            int i = 0;
            for (Serializable field : fields) {
                this.sqlBuilder.addMappingField(new SQLMappingField(field));
                i++;
                if (i != fields.length) this.sqlBuilder.addSplit();
            }
        }
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable... fields) {
        if (fields != null) {
            this.sqlBuilder.addWrapString(tableAliasName);
            int i = 0;
            for (Serializable field : fields) {
                this.sqlBuilder.addMappingField(new SQLMappingField(field));
                i++;
                if (i != fields.length) this.sqlBuilder.addSplit();
            }
        }
        return this;
    }

    @Override
    public Object field(Class table, Serializable field, String fieldAliasName) {
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        return this;
    }

    @Override
    public Object field(String tableAliasName, Serializable field, String fieldAliasName) {
        this.sqlBuilder.addMappingField(new SQLMappingField(tableAliasName, field));
        this.sqlBuilder.AS().addWrapString(fieldAliasName);
        return this;
    }

    @Override
    public Object table(Class table) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        return this;
    }

    @Override
    public Object value(Object value) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName, value);
        return this;
    }

    @Override
    public Object column(Serializable field) {
        if (this.body == 3 && this.hasLeastOneSort) this.sqlBuilder.addSplit();
        this.sqlBuilder.addWrapString(field.toString());
        this.lastPlaceholderName = field.toString();
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        if (this.body == 3 && this.hasLeastOneSort) this.sqlBuilder.addSplit();
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addTableWrapField(mappingTable.getMappingTableName(), field.toString());
        this.lastPlaceholderName = mappingTable.getMappingTableName() + "." + field.toString();
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        if (this.body == 3 && this.hasLeastOneSort) this.sqlBuilder.addSplit();
        this.sqlBuilder.addTableWrapField(aliasName, field.toString());
        this.lastPlaceholderName = aliasName + "." + field.toString();
        return this;
    }

    @Override
    public Object and() {
        this.sqlBuilder.AND();
        return this;
    }

    @Override
    public Object count(FieldItem fieldItem) {
        this.sqlBuilder.addString("COUNT");
        this.sqlBuilder.addParenthesisStart();
        this.setFieldItemA(fieldItem);
        this.sqlBuilder.addParenthesisEnd();
        this.setFieldItemB(fieldItem);
        return this;
    }

    @Override
    public Object max(FieldItem fieldItem) {
        this.sqlBuilder.addString("MAX");
        this.sqlBuilder.addParenthesisStart();
        this.setFieldItemA(fieldItem);
        this.sqlBuilder.addParenthesisEnd();
        this.setFieldItemB(fieldItem);
        return this;
    }

    @Override
    public Object avg(FieldItem fieldItem) {
        this.sqlBuilder.addString("AVG");
        this.sqlBuilder.addParenthesisStart();
        this.setFieldItemA(fieldItem);
        this.sqlBuilder.addParenthesisEnd();
        this.setFieldItemB(fieldItem);
        return this;
    }

    @Override
    public Object sum(FieldItem fieldItem) {
        this.sqlBuilder.addString("SUM");
        this.sqlBuilder.addParenthesisStart();
        this.setFieldItemA(fieldItem);
        this.sqlBuilder.addParenthesisEnd();
        this.setFieldItemB(fieldItem);
        return this;
    }

    @Override
    public Object min(FieldItem fieldItem) {
        this.sqlBuilder.addString("MIN");
        this.sqlBuilder.addParenthesisStart();
        this.setFieldItemA(fieldItem);
        this.sqlBuilder.addParenthesisEnd();
        this.setFieldItemB(fieldItem);
        return this;
    }

    @Override
    public Object concat(FieldItem... fieldItems) {
        this.concat(null, fieldItems);
        return this;
    }

    @Override
    public Object concat(String fieldAliasName, FieldItem... fieldItems) {
        if (fieldItems != null) {
            this.sqlBuilder.addString("CONCAT");
            this.sqlBuilder.addParenthesisStart();
            int i = 0;
            for (FieldItem fieldItem : fieldItems) {
                this.setFieldItemA(fieldItem);
                i++;
                if (i != fieldItems.length) this.sqlBuilder.addSplit();
            }
            this.sqlBuilder.addParenthesisEnd();
            if (StringTools.isNotEmpty(fieldAliasName)) {
                this.sqlBuilder.AS().addWrapString(fieldAliasName);
            }
        }
        return this;
    }

    @Override
    public Object substring(FieldItem fieldItem, int pos, int len) {
        this.sqlBuilder.addString("SUBSTRING");
        this.sqlBuilder.addParenthesisStart();
        this.setFieldItemA(fieldItem);
        this.sqlBuilder.addSplit().addString("" + pos).addSplit().addString("" + len);
        this.sqlBuilder.addParenthesisEnd();
        this.setFieldItemB(fieldItem);
        return this;
    }

    @Override
    public Object from() {
        this.sqlBuilder.FROM();
        return this;
    }

    @Override
    public Object having() {
        this.sqlBuilder.HAVING();
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.sqlBuilder.LIMIT().addString(pos + "," + len);
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        this.sqlBuilder.addString("ISNULL");
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addWrapString(field.toString());
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addString("ISNULL");
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addTableWrapField(mappingTable.getMappingTableName(), field.toString());
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        this.sqlBuilder.addString("ISNULL");
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addTableWrapField(aliasName, field.toString());
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        this.sqlBuilder.NOT();
        this.isNull(field);
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        this.sqlBuilder.NOT();
        this.isNull(table, field);
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        this.sqlBuilder.NOT();
        this.isNull(aliasName, field);
        return this;
    }

    @Override
    public Object in() {
        this.sqlBuilder.IN();
        return this;
    }

    @Override
    public Object nin() {
        this.sqlBuilder.NOT().IN();
        return this;
    }

    @Override
    public Object like() {
        this.sqlBuilder.LIKE();
        return this;
    }

    @Override
    public Object ne() {
        this.sqlBuilder.addString("!=");
        return this;
    }

    @Override
    public Object gt() {
        this.sqlBuilder.addString(">");
        return this;
    }

    @Override
    public Object gte() {
        this.sqlBuilder.addString(">=");
        return this;
    }

    @Override
    public Object lt() {
        this.sqlBuilder.addString("<");
        return this;
    }

    @Override
    public Object lte() {
        this.sqlBuilder.addString("<=");
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
        this.sqlBuilder.BETWEEN();
        return this;
    }

    @Override
    public BetweenValueBuilder notBetween() {
        this.sqlBuilder.NOT().BETWEEN();
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName + "&1", valueA);
        this.sqlBuilder.AND();
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName + "&2", valueB);
        return this;
    }

    @Override
    public Object eq() {
        this.sqlBuilder.addString("=");
        return this;
    }

    @Override
    public Object or() {
        this.sqlBuilder.OR();
        return this;
    }

    @Override
    public Object orderBy() {
        this.sqlBuilder.ORDER().BY();
        return this;
    }

    @Override
    public Object asc() {
        this.sqlBuilder.ASC();
        return this;
    }

    @Override
    public Object desc() {
        this.sqlBuilder.DESC();
        return this;
    }

    @Override
    public Object where() {
        this.sqlBuilder.WHERE();
        return this;
    }

    @Override
    public Object wrapper(AboutChildBuilder builder) {
        SQLBuilder sqlBuilder = builder.getSqlBuilder();
        sqlBuilder.setTableFieldReplaceRule(this.sqlBuilder.getRuleStart(), this.sqlBuilder.getRuleFinish());
        this.sqlBuilder.addParenthesisStart();
        this.sqlBuilder.addSQLBuilder(sqlBuilder);
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object inner() {
        this.sqlBuilder.INNER();
        return this;
    }

    @Override
    public Object join() {
        this.sqlBuilder.JOIN();
        return this;
    }

    @Override
    public Object left() {
        this.sqlBuilder.LEFT();
        return this;
    }

    @Override
    public Object on() {
        this.sqlBuilder.ON();
        return this;
    }

    @Override
    public Object split() {
        this.sqlBuilder.addSplit();
        return this;
    }

    @Override
    public Object as(String tableAliasName) {
        this.sqlBuilder.AS().addWrapString(tableAliasName);
        return this;
    }
}
