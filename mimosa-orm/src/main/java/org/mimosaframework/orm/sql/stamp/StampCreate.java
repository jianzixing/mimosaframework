package org.mimosaframework.orm.sql.stamp;

public class StampCreate implements StampTables {
    public KeyTarget target;
    public boolean checkExist = false;

    public String name;
    public Class table;

    public StampCreateColumn[] columns;
    public StampCreateIndex[] indices;

    public String charset;
    public String collate;
    public String extra;

    public String indexName;
    public StampColumn[] indexColumns;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
