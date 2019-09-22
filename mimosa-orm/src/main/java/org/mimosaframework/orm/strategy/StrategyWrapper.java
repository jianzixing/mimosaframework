package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

public class StrategyWrapper {
    private ContextWrapper context;
    private Class tableClass;
    private String tableName;
    private String dbTableName;
    private String field;
    private String dbField;

    public StrategyWrapper(ContextWrapper context) {
        this.context = context;
    }

    public Class getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class tableClass) {
        this.tableClass = tableClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public String getDbField() {
        return dbField;
    }

    public void setDbField(String dbField) {
        this.dbField = dbField;
    }

    public ActionDataSourceWrapper getNewDataSourceWrapper() {
        return this.context.getDefaultDataSourceWrapper(true);
    }

    public ContextWrapper getContext() {
        return this.context;
    }
}
