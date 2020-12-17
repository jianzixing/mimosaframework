package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.Configuration;

public class StrategyWrapper {
    private Configuration configuration;
    private Class tableClass;
    private String tableName;
    private String dbTableName;
    private String field;
    private String dbField;

    public StrategyWrapper(Configuration configuration) {
        this.configuration = configuration;
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

    public Configuration getConfiguration() {
        return this.configuration;
    }
}
