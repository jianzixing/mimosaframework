package org.mimosaframework.orm.sql.stamp;

public class StampDelete {
    public Class tables[];
    public String aliasNames[];

    public StampFrom[] froms;

    public StampFrom[] using;

    public StampWhere where;

    public StampOrderBy[] orderBy;

    public StampLimit limit;
}
