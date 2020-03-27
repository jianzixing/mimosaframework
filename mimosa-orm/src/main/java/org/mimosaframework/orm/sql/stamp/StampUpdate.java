package org.mimosaframework.orm.sql.stamp;

public class StampUpdate {
    public Class[] tables;
    public String tableNames;

    public StampUpdateItem[] items;

    public StampWhere where;
    public StampOrderBy[] orderBy;
    public StampLimit limit;
}
