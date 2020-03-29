package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampAlter implements StampAction {
    public KeyTarget target;

    public String name;
    public Class table;

    public StampAlterItem[] items;

    public String charset;
    public String collate;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (table != null) {
            items.add(new STItem(table));
        }
        return items;
    }
}
