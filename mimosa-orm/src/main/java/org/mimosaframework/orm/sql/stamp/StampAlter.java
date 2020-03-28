package org.mimosaframework.orm.sql.stamp;

public class StampAlter implements StampTables {
    public KeyTarget target;

    public String name;
    public Class table;

    public StampAlterItem[] items;


    public String charset;
    public String collate;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
