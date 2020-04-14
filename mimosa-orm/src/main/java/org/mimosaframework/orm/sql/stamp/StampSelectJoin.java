package org.mimosaframework.orm.sql.stamp;

import org.mimosaframework.orm.sql.UnifyBuilder;

public class StampSelectJoin {
    public KeyJoinType joinType;
    public StampWhere on;

    public String tableName;
    public Class tableClass;

    public String tableAliasName;

    public UnifyBuilder builder;
}
