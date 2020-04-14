package org.mimosaframework.orm.sql.stamp;

import org.mimosaframework.orm.sql.UnifyBuilder;

public class StampFrom {
    public Class table;
    public String name;
    public String aliasName;

    public UnifyBuilder builder;

    public StampFrom(UnifyBuilder builder) {
        this.builder = builder;
    }

    public StampFrom(Class table) {
        this.table = table;
    }

    public StampFrom(String name) {
        this.name = name;
    }

    public StampFrom(Class table, String aliasName) {
        this.table = table;
        this.aliasName = aliasName;
    }

    public StampFrom(String name, String aliasName) {
        this.name = name;
        this.aliasName = aliasName;
    }
}
