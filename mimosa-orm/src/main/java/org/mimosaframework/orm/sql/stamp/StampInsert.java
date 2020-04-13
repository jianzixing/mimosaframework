package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampInsert implements StampAction {
    public String tableName;
    public Class tableClass;

    public StampColumn[] columns;
    public Object[][] values;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (tableClass != null) {
            items.add(new STItem(tableClass));
        }
        return items;
    }
}
