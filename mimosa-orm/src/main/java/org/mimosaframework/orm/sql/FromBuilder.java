package org.mimosaframework.orm.sql;

import java.util.ArrayList;
import java.util.List;

public class FromBuilder {
    private Class table;
    private List<Object> fields;

    public FromBuilder(Class table, Object... fields) {
        this.table = table;
        this.fields = new ArrayList<>();
        for (Object o : fields) {
            this.fields.add(o);
        }
    }

    public FromBuilder(Class table, List<Object> fields) {
        this.table = table;
        this.fields = fields;
    }

    public FromBuilder(Class table) {
        this.table = table;
    }

    public Class getTable() {
        return table;
    }

    public List<Object> getFields() {
        return fields;
    }

    public static FromBuilder builder(Class table, Object... fields) {
        return new FromBuilder(table, fields);
    }

    public static FromBuilder builder(Class table, List<Object> fields) {
        return new FromBuilder(table, fields);
    }

    public static FromBuilder builder(Class table) {
        return new FromBuilder(table);
    }

    public FromBuilder clone() {
        FromBuilder builder = new FromBuilder(null);
        if (fields != null) builder.fields = new ArrayList<>(fields);
        if (table != null) builder.table = table;

        return builder;
    }
}
