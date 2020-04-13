package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampDrop implements StampAction {
    public KeyTarget target;
    public boolean checkExist = false;

    public String databaseName;

    public String tableName;
    public Class tableClass;

    public String indexName;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (tableClass != null) {
            items.add(new STItem(tableClass));
        }
        return items;
    }
}
