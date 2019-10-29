package org.mimosaframework.orm.sql;

import java.util.List;

public class GroupBuilder {
    private Class table;
    private List<Object> fields;

    public GroupBuilder(List<Object> fields) {
        this.fields = fields;
    }

    public GroupBuilder(Class table, List<Object> fields) {
        this.table = table;
        this.fields = fields;
    }

    public Class getTable() {
        return table;
    }

    public List<Object> getFields() {
        return fields;
    }
}
