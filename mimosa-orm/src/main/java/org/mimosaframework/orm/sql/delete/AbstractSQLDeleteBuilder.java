package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLDeleteBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineDeleteBuilder {

    protected boolean hasLeastOneSort = false;

    @Override
    public Object delete() {
        this.sqlBuilder.DELETE();
        return this;
    }

    @Override
    public Object table(Class table) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table, tableAliasName));
        return this;
    }

    @Override
    public Object from() {
        this.body = 1;
        this.sqlBuilder.FROM();
        return this;
    }

    @Override
    public Object where() {
        this.sqlBuilder.WHERE();
        this.body = 2;
        return this;
    }

    @Override
    public Object column(Serializable field) {
        if (this.body == 2 || this.body == 3) {
            if (this.hasLeastOneSort) this.sqlBuilder.addSplit();
            this.sqlBuilder.addWrapString(field.toString());
            this.lastPlaceholderName = field.toString();
        } else {

        }
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        if (this.body == 2 || this.body == 3) {
            if (this.hasLeastOneSort) this.sqlBuilder.addSplit();
            MappingTable mappingTable = this.getMappingTableByClass(table);
            this.sqlBuilder.addTableWrapField(mappingTable.getMappingTableName(), field.toString());
            this.lastPlaceholderName = mappingTable.getMappingTableName() + "." + field.toString();
        } else {

        }
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        if (this.body == 2 || this.body == 3) {
            if (this.hasLeastOneSort) this.sqlBuilder.addSplit();
            this.sqlBuilder.addTableWrapField(aliasName, field.toString());
            this.lastPlaceholderName = aliasName + "." + field.toString();
        } else {

        }
        return this;
    }

    @Override
    public Object eq() {
        this.sqlBuilder.addString("=");
        return this;
    }

    @Override
    public Object value(Object value) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName, value);
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
    public Object and() {
        this.sqlBuilder.AND();
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.sqlBuilder.LIMIT();
        this.sqlBuilder.addDataPlaceholder("limit&pos", pos);
        this.sqlBuilder.addSplit();
        this.sqlBuilder.addDataPlaceholder("limit&len", len);
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
        this.body = 3;
        return this;
    }

    @Override
    public Object table(String... aliasNames) {
        int i = 0;
        for (String aliasName : aliasNames) {
            i++;
            if (i == aliasNames.length) {
                this.sqlBuilder.addString(aliasName);
            } else {
                this.sqlBuilder.addString(aliasName).addSplit();
            }
        }
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
    public Object table(Class... table) {
        // if (this.body == 0) {
        int i = 0;
        for (Class tb : table) {
            i++;
            if (i == table.length) {
                this.sqlBuilder.addMappingTable(new SQLMappingTable(tb));
            } else {
                this.sqlBuilder.addMappingTable(new SQLMappingTable(tb));
                this.sqlBuilder.addSplit();
            }
        }
        return this;
    }

    @Override
    public Object asc() {
        this.sqlBuilder.ASC();
        this.hasLeastOneSort = true;
        return this;
    }

    @Override
    public Object desc() {
        this.sqlBuilder.DESC();
        this.hasLeastOneSort = true;
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(aliasName, field));
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(aliasName, field));
        return this;
    }

    @Override
    public Object using() {
        this.sqlBuilder.USING();
        return this;
    }
}
