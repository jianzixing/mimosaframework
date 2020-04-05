package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampCreate implements StampAction {
    public KeyTarget target;
    public boolean checkExist = false;

    public String name;
    public Class table;

    public StampCreateColumn[] columns;
    public StampCreateIndex[] indices;

    public String charset;
    public String collate;
    public String extra;

    public KeyIndexType indexType;
    public String indexName;
    public StampColumn[] indexColumns;

    public String comment;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = new ArrayList<>();
        if (table != null) {
            items.add(new STItem(table));
        }
        return items;
    }
}