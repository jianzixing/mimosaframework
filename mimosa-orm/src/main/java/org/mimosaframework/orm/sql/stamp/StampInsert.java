package org.mimosaframework.orm.sql.stamp;

public class StampInsert {
    public String name;
    public Class table;

    public StampColumn[] columns;
    public Object[][] values;
}
