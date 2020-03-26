package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLUpdateBuilder
        extends
        AbstractValueSQLBuilder

        implements
        RedefineUpdateBuilder {

    protected boolean hasLeastOneSort = false;
    protected boolean hasSetValue = false;
    protected boolean hasTable = false;

    @Override
    public Object update() {
        this.sqlBuilder.UPDATE();
        return this;
    }

    @Override
    public Object table(Class table) {
        if (this.hasTable) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        this.body = 1;
        this.hasTable = true;
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        if (this.hasTable) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table, tableAliasName));
        this.sqlBuilder.AS().addWrapString(tableAliasName);
        this.hasTable = true;
        this.body = 1;
        return this;
    }

    @Override
    public Object column(Serializable field) {
        if (this.body == 3 && this.hasLeastOneSort) this.sqlBuilder.addSplit();
        if (this.body == 2 && this.hasSetValue) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        this.lastPlaceholderName = field.toString();
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        if (this.body == 4 && this.hasLeastOneSort) this.sqlBuilder.addSplit();
        if (this.body == 2 && this.hasSetValue) this.sqlBuilder.addSplit();
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addMappingField(new SQLMappingField(table, field));
        this.lastPlaceholderName = mappingTable.getMappingTableName() + "." + field.toString();
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        if (this.body == 4 && this.hasLeastOneSort) this.sqlBuilder.addSplit();
        if (this.body == 2 && this.hasSetValue) this.sqlBuilder.addSplit();
        this.sqlBuilder.addMappingField(new SQLMappingField(aliasName, field));
        this.lastPlaceholderName = aliasName + "." + field.toString();
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        this.sqlBuilder.LIMIT().addString(pos + "," + len);
        return this;
    }

    @Override
    public Object eq() {
        this.sqlBuilder.addString("=");
        return this;
    }

    @Override
    public Object orderBy() {
        this.sqlBuilder.ORDER().BY();
        this.body = 4;
        return this;
    }

    @Override
    public Object set() {
        this.sqlBuilder.SET();
        this.body = 2;
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
    public Object where() {
        this.sqlBuilder.WHERE();
        this.body = 3;
        return this;
    }

    @Override
    public Object value(Object value) {
        super.value(value);
        if (this.body == 2) {
            this.hasSetValue = true;
        }
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
}
