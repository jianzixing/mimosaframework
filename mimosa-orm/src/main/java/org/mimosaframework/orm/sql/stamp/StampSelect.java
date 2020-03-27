package org.mimosaframework.orm.sql.stamp;

public class StampSelect {
    public StampSelectField[] columns;
    public StampFrom[] froms;
    public StampWhere where;
    public StampGroupBy groupBy;
    public StampHaving having;
    public StampOrderBy orderBy;
    public StampLimit limit;
}
