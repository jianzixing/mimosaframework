package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampAlter implements StampAction {
    public KeyTarget target;

    public String tableName;
    public Class tableClass;

    public String databaseName;

    public StampAlterItem[] items;

    public String charset;
    public String collate;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (tableClass != null) {
            items.add(new STItem(tableClass));
        }
        return items;
    }
}
