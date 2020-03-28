package org.mimosaframework.orm.sql.stamp;

public class StampUpdate implements StampTables {
    public Class[] tables;
    public String tableNames;

    public StampUpdateItem[] items;

    public StampWhere where;
    public StampOrderBy[] orderBy;
    public StampLimit limit;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
