package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampUpdate implements StampAction {
    public StampFrom[] tables;

    public StampUpdateItem[] items;

    public StampWhere where;
    public StampOrderBy[] orderBy;
    public StampLimit limit;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (tables != null) {
            for (StampFrom table : tables) {
                if (table.table != null) {
                    items.add(new STItem(table.table, table.aliasName));
                }
            }
        }
        return items;
    }
}
