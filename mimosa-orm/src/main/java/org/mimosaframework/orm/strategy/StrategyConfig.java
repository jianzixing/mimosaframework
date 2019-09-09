package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.MimosaDataSource;

public class StrategyConfig {
    private Class tableClass;
    private MimosaDataSource dataSource;
    /**
     * 自增ID的时候如果没有自定义ID策略表则默认
     */
    private String tableName;
    /**
     * 自增的字段，如果没有则表示所有字段都可以使用本配置
     * 如果已经配置表示一个表中的这个字段才能使用当前配置
     */
    private String field;

    public StrategyConfig(Class tableClass, MimosaDataSource dataSource) {
        this.tableClass = tableClass;
        this.dataSource = dataSource;
    }

    public StrategyConfig() {
    }

    public Class getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class tableClass) {
        this.tableClass = tableClass;
    }


    public MimosaDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
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
}
