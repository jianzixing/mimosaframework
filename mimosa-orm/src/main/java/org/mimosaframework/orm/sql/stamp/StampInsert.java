package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampInsert implements StampAction {
    public String name;
    public Class table;

    public StampColumn[] columns;
    public Object[][] values;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (table != null) {
            items.add(new STItem(table));
        }
        return items;
    }
}
