package org.mimosaframework.orm.platform;

public class TableColumnStructure {
    private String tableSchema;
    private String tableName;
    private String columnName;
    private String typeName;
    private int length = -1;
    private int scale = -1;
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
}
