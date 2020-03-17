package org.mimosaframework.orm.sql.test;

import java.util.ArrayList;
import java.util.List;

public class FieldItems {
    private List<FieldItem> fieldItems;

    public static FieldItems build() {
        return new FieldItems();
    }

    public FieldItems field(FieldItem fieldItem) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(fieldItem);
        return this;
    }

    public FieldItems field(Class table, Object field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(table, field));
        return this;
    }

    public FieldItems field(String tableAliasName, Object field) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(tableAliasName, field));
        return this;
    }

    public FieldItems field(Class table, Object field, String fieldAliasName) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(table, field, fieldAliasName));
        return this;
    }

    public FieldItems field(String tableAliasName, Object field, String fieldAliasName) {
        if (this.fieldItems == null) this.fieldItems = new ArrayList<>();
        this.fieldItems.add(new FieldItem(tableAliasName, field, fieldAliasName));
        return this;
    }
}
