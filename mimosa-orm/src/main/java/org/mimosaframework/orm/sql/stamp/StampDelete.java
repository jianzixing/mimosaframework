package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampDelete implements StampAction {
    public StampFrom from;
    public StampWhere where;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = null;
        if (from != null && from.table != null) {
            if (items == null) items = new ArrayList<>();
            items.add(new STItem(from.table, from.aliasName));
        }
        return items;
    }
}
