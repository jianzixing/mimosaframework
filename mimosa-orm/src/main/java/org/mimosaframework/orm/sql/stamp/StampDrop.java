package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampDrop implements StampAction {
    public KeyTarget target;
    public boolean checkExist = false;

    public String name;
    public Class table;

    public String tableName;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (table != null) {
            items.add(new STItem(table));
        }
        return items;
    }
}
