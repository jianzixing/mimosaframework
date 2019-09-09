package org.mimosaframework.orm.platform;

public class SelectFieldAliasReference {
    private String tableName;
    private String tableAliasName;
    private String fieldName;
    private String fieldAliasName;

    private Class<?> mainClass;
    private Class<?> tableClass;
    private String javaFieldName;
    private boolean isPrimaryKey = false;

    /**
     * 构造一个select语句的字段结构
     * 构造后{@link #mainClass}和{@link #tableClass}组合得出一个完整的join区分
     *
     * @param mainClass  join的主表
     * @param tableClass 需要join的表
     */
    public SelectFieldAliasReference(Class<?> mainClass, Class<?> tableClass) {
        this.mainClass = mainClass;
        this.tableClass = tableClass;
    }

    public Class<?> getMainClass() {
        return mainClass;
    }

    public void setMainClass(Class<?> mainClass) {
        this.mainClass = mainClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldAliasName() {
        return fieldAliasName;
    }

    public void setFieldAliasName(String fieldAliasName) {
        this.fieldAliasName = fieldAliasName;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class<?> tableClass) {
        this.tableClass = tableClass;
    }

    public void setJavaFieldName(String javaFieldName) {
        this.javaFieldName = javaFieldName;
    }

    public String getJavaFieldName() {
        return javaFieldName;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
}
