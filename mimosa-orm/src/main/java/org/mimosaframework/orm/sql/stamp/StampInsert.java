package org.mimosaframework.orm.sql.stamp;

public class StampInsert implements StampTables {
    public String name;
    public Class table;

    public StampColumn[] columns;
    public Object[][] values;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
