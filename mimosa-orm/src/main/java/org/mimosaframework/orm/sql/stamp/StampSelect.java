package org.mimosaframework.orm.sql.stamp;

public class StampSelect implements StampTables {
    public StampSelectField[] columns;
    public StampFrom[] froms;
    public StampWhere where;
    public StampGroupBy groupBy;
    public StampHaving having;
    public StampOrderBy orderBy;
    public StampLimit limit;

    @Override
    public Class[] getTables() {
        return new Class[0];
    }
}
