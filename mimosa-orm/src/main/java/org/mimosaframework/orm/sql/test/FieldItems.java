package org.mimosaframework.orm.sql.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FieldItems implements AboutFieldItem<FieldItems> {
    private List<FieldItem> fieldItems;

    public static FieldItems build() {
        return new FieldItems();
    }

    public static FieldFunBuilder<FieldFunBuilder> function() {
        return null;
    }

    private FieldItems field(FieldItem fieldItem) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(fieldItem);
        return this;
    }

    public FieldItems field(Serializable field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(field));
        return this;
    }

    public FieldItems field(Class table, Serializable field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(table, field));
        return this;
    }

    public FieldItems field(String tableAliasName, Serializable field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(tableAliasName, field));
        return this;
    }

    public FieldItems field(Class table, Serializable field, String fieldAliasName) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(table, field, fieldAliasName));
        return this;
    }

    public FieldItems field(String tableAliasName, Serializable field, String fieldAliasName) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(tableAliasName, field, fieldAliasName));
        return this;
    }

    @Override
    public FieldItems fun(FieldFunBuilder funBuilder) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(funBuilder));
        return this;
    }
}
