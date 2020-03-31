package org.mimosaframework.orm.sql.stamp;

import java.io.Serializable;

public class StampColumn {
    public Class table;
    public String tableAliasName;
    public Serializable column;

    public StampColumn() {
    }

    public StampColumn(Serializable column) {
        this.column = column;
    }

    public StampColumn(Class table, Serializable column) {
        this.table = table;
        this.column = column;
    }

    public StampColumn(String tableAliasName, Serializable column) {
        this.tableAliasName = tableAliasName;
        this.column = column;
    }
}
