package org.mimosaframework.orm.sql;

public class FunBuilder {
    private Class table;
    private Object field;
    private FunType funType;
    private String aliasName;

    public FunBuilder(Object field, FunType funType) {
        this.field = field;
        this.funType = funType;
    }

    public FunBuilder(Class table, Object field, FunType funType) {
        this.table = table;
        this.field = field;
        this.funType = funType;
    }

    public FunBuilder(Object field, FunType funType, String aliasName) {
        this.aliasName = aliasName;
        this.field = field;
        this.funType = funType;
    }

    public FunBuilder(Class table, Object field, FunType funType, String aliasName) {
        this.aliasName = aliasName;
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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public static FunBuilder builder(Class table, Object field, FunType funType) {
        return new FunBuilder(table, field, funType);
    }

    public static FunBuilder builder(Object field, FunType funType) {
        return new FunBuilder(field, funType);
    }

    public static FunBuilder builder(Class table, Object field, FunType funType, String aliasName) {
        return new FunBuilder(table, field, funType, aliasName);
    }

    public static FunBuilder builder(Object field, FunType funType, String aliasName) {
        return new FunBuilder(field, funType, aliasName);
    }
}
