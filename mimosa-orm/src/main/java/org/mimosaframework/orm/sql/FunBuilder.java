package org.mimosaframework.orm.sql;

public class FunBuilder {
    private Class table;
    private Object field;
    private FunType funType;

    public FunBuilder(Class table, Object field, FunType funType) {
        this.table = table;
        this.field = field;
        this.funType = funType;
    }

    public Class getTable() {
        return table;
    }

    public Object getField() {
        return field;
    }

    public FunType getFunType() {
        return funType;
    }

    public static FunBuilder builder(Class table, Object field, FunType funType) {
        return new FunBuilder(table, field, funType);
    }
}
