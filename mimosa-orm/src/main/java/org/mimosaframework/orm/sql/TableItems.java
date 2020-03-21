package org.mimosaframework.orm.sql;

import java.util.ArrayList;
import java.util.List;

public class TableItems {
    private List<TableItem> tableItems;

    public static TableItems build() {
        return new TableItems();
    }

    public TableItems table(Class table) {
        if (tableItems == null) tableItems = new ArrayList<>();
        tableItems.add(new TableItem(table));
        return this;
    }

    public TableItems table(Class table, String aliasName) {
        if (tableItems == null) tableItems = new ArrayList<>();
        tableItems.add(new TableItem(table, aliasName));
        return this;
    }

    public List<TableItem> getTableItems() {
        return tableItems;
    }
}
