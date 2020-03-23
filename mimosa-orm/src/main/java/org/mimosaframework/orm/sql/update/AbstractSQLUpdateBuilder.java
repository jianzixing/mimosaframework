package org.mimosaframework.orm.sql.update;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLUpdateBuilder
        extends
        AbstractSQLBuilder

        implements
        RedefineUpdateBuilder {

    protected boolean hasLeastOneSort = false;

    @Override
    public Object update() {
        this.sqlBuilder.UPDATE();
        return this;
    }

    @Override
    public Object table(Class... tables) {
        if (tables != null) {
            int i = 0;
            for (Class table : tables) {
                MappingTable mappingTable = this.getMappingTableByClass(table);
                i++;
                this.sqlBuilder.addString(mappingTable.getMappingTableName());
                if (i != tables.length) this.sqlBuilder.addSplit();
            }
        }
        this.body = 1;
        return this;
    }

    @Override
    public Object table(TableItem tableItem) {
        Class table = tableItem.getTable();
        String aliasName = tableItem.getAliasName();

        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addString(mappingTable.getMappingTableName());
        if (StringTools.isNotEmpty(aliasName)) this.sqlBuilder.AS().addString(aliasName);
        this.body = 1;
        return this;
    }

    @Override
    public Object table(TableItem... tableItems) {
        if (tableItems != null) {
            int i = 0;
            for (TableItem tableItem : tableItems) {
                Class table = tableItem.getTable();
                String aliasName = tableItem.getAliasName();
                MappingTable mappingTable = this.getMappingTableByClass(table);
                i++;
                this.sqlBuilder.addString(mappingTable.getMappingTableName());
                if (StringTools.isNotEmpty(aliasName)) this.sqlBuilder.AS().addString(aliasName);
                if (i != tableItems.length) this.sqlBuilder.addSplit();
            }
        }
        this.body = 1;
        return this;
    }

    @Override
    public Object table(TableItems tableItems) {
        this.table(tableItems.getTableItems().toArray(new TableItem[]{}));
        this.body = 1;
        return this;
    }

    @Override
    public Object table(Class table) {
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addString(mappingTable.getMappingTableName());
        this.body = 1;
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
        this.body = 3;
        return this;
    }

    @Override
    public Object set() {
        this.sqlBuilder.SET();
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
    public Object split() {
        this.sqlBuilder.addSplit();
        return this;
    }

    @Override
    public Object where() {
        this.sqlBuilder.WHERE();
        this.body = 2;
        return this;
    }

    @Override
    public Object value(Object value) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName, value);
        return this;
    }
}
