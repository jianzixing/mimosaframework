package org.mimosaframework.orm.sql.stamp;

public class StampSelectJoin {
    public KeyJoinType joinType;
    public StampWhere on;

    public String name;
    public Class table;

    public String tableAliasName;
}
