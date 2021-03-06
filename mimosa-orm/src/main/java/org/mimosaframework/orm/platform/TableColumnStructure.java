package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;

public class TableColumnStructure {
    private String tableSchema;
    private String tableName;
    private String columnName;
    private String typeName;
    private int length = 0;
    private int scale = 0;
    private String defaultValue;
    /**
     * Y or N
     */
    private String isNullable;
    /**
     * Y or N
     */
    private String autoIncrement;
    private String comment;

    /**
     * 提供字段增删改时使用
     * 用来标记字段的状态
     * 0 无状态
     * 1 修改
     * 2 删除
     */
    private int state = 0;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public boolean isNullable() {
        if (StringTools.isEmpty(isNullable)) return true;
        return "Y".equals(isNullable);
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getAutoIncrement() {
        return autoIncrement;
    }

    public boolean isAutoIncrement() {
        return "Y".equals(autoIncrement);
    }

    public void setAutoIncrement(String autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
