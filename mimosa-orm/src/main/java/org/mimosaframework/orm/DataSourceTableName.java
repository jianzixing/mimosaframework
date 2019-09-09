package org.mimosaframework.orm;

import java.util.List;

public class DataSourceTableName {
    private String dataSourceName;
    private String tableName;
    private String hashFieldName;
    private List<String> slaves;

    public DataSourceTableName() {
    }

    public DataSourceTableName(String dataSourceName, String tableName) {
        this.dataSourceName = dataSourceName;
        this.tableName = tableName;
    }

    public DataSourceTableName(String dataSourceName, String tableName, String hashFieldName) {
        this.dataSourceName = dataSourceName;
        this.tableName = tableName;
        this.hashFieldName = hashFieldName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getHashFieldName() {
        return hashFieldName;
    }

    public void setHashFieldName(String hashFieldName) {
        this.hashFieldName = hashFieldName;
    }

    public List<String> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<String> slaves) {
        this.slaves = slaves;
    }
}
