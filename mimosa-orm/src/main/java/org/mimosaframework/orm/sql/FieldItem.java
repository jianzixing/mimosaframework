package org.mimosaframework.orm.sql;

import java.io.Serializable;

public class FieldItem implements AboutFieldItem<FieldItem> {
    private Class table;
    private String tableAliasName;
    private Serializable field;
    private String fieldAliasName;

    private FieldFunBuilder fun;

    public FieldItem(FieldFunBuilder fun) {
        this.fun = fun;
    }

    public FieldItem(Serializable field) {
        this.field = field;
    }

    public FieldItem(Class table, Serializable field) {
        this.table = table;
        this.field = field;
    }

    public FieldItem(String tableAliasName, Serializable field) {
        this.tableAliasName = tableAliasName;
        this.field = field;
    }

    public FieldItem(Class table, Serializable field, String fieldAliasName) {
        this.table = table;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
    }

    public FieldItem(String tableAliasName, Serializable field, String fieldAliasName) {
        this.tableAliasName = tableAliasName;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public Serializable getField() {
        return field;
    }

    public void setField(Serializable field) {
        this.field = field;
    }

    public String getFieldAliasName() {
        return fieldAliasName;
    }

    public void setFieldAliasName(String fieldAliasName) {
        this.fieldAliasName = fieldAliasName;
    }

    @Override
    public FieldItem field(Serializable field) {
        this.field = field;
        return this;
    }

    @Override
    public FieldItem field(String tableAliasName, Serializable field) {
        this.tableAliasName = tableAliasName;
        this.field = field;
        return this;
    }

    @Override
    public FieldItem field(Class table, Serializable field) {
        this.table = table;
        this.field = field;
        return this;
    }

    @Override
    public FieldItem field(Class table, Serializable field, String fieldAliasName) {
        this.table = table;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
        return this;
    }

    @Override
    public FieldItem field(String tableAliasName, Serializable field, String fieldAliasName) {
        this.tableAliasName = tableAliasName;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
        return this;
    }

    @Override
    public FieldItem fun(FieldFunBuilder funBuilder) {
        return this;
    }
}
