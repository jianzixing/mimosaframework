package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampUpdate implements StampAction {
    public StampFrom table;

    public StampUpdateItem[] items;
    public StampWhere where;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = null;
        if (table != null && table.table != null) {
            if (items == null) items = new ArrayList<>();
            items.add(new STItem(table.table, table.aliasName));
        }
        return items;
    }
}
