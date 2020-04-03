package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampUpdate implements StampAction {
    public StampFrom table;
    public StampFrom[] froms;

    public StampUpdateItem[] items;

    public StampWhere where;
    public StampOrderBy[] orderBy;
    public StampLimit limit;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (froms != null) {
            for (StampFrom table : froms) {
                if (table.table != null) {
                    items.add(new STItem(table.table, table.aliasName));
                }
            }
        }
        if (table != null) {
            items.add(new STItem(table.table, table.aliasName));
        }
        return items;
    }
}
