package org.mimosaframework.orm.sql.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fields implements AboutFieldItem<Fields> {
    private List<FieldItem> fieldItems;

    public static Fields build() {
        return new Fields();
    }

    public static FieldFunBuilder<FieldFunBuilder> function() {
        return null;
    }

    private Fields field(FieldItem fieldItem) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(fieldItem);
        return this;
    }

    public Fields field(Serializable field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(field));
        return this;
    }

    public Fields field(Class table, Serializable field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(table, field));
        return this;
    }

    public Fields field(String tableAliasName, Serializable field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(tableAliasName, field));
        return this;
    }

    public Fields field(Class table, Serializable field, String fieldAliasName) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(table, field, fieldAliasName));
        return this;
    }

    public Fields field(String tableAliasName, Serializable field, String fieldAliasName) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(tableAliasName, field, fieldAliasName));
        return this;
    }

    @Override
    public Fields fun(FieldFunBuilder funBuilder) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(funBuilder));
        return this;
    }
}
