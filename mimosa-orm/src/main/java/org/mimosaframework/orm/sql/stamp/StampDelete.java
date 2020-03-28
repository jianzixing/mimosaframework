package org.mimosaframework.orm.sql.stamp;

public class StampDelete implements StampTables {
    public Class tables[];
    public String aliasNames[];

    public StampFrom[] froms;

    public StampFrom[] using;

    public StampWhere where;

    public StampOrderBy[] orderBy;

    public StampLimit limit;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
