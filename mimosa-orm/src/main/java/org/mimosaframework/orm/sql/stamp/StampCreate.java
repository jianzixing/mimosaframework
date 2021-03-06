package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampCreate implements StampAction {
    public KeyTarget target;
    public boolean checkExist = false;

    public String tableName;
    public Class tableClass;

    public String databaseName;

    public StampCreateColumn[] columns;
    public StampCreatePrimaryKey primaryKey;

    public String charset;
    public String collate;
    public String extra;

    public KeyIndexType indexType;
    public String indexName;
    public StampColumn[] indexColumns;

    public String comment;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = null;
        if (tableClass != null) {
            if (items == null) items = new ArrayList<>();
            items.add(new STItem(tableClass));
        }
        return items;
    }
}
