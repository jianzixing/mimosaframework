package org.mimosaframework.orm.sql.stamp;

public class StampDrop implements StampTables {
    public KeyTarget target;
    public boolean checkExist = false;

    public String name;
    public Class table;

    public String tableName;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
