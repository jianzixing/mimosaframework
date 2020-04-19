package org.mimosaframework.orm.platform;

public class TableIndexStructure {
    private String tableSchema;
    private String indexName;
    private String tableName;
    /**
     * U union
     * P primary
     * D normal
     */
    private String type;
    private String columnName;
    private String comment;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public TableIndexStructure clone() {
        TableIndexStructure structure = new TableIndexStructure();
        structure.tableSchema = tableSchema;
        structure.indexName = indexName;
        structure.tableName = tableName;
        structure.type = type;
        structure.columnName = columnName;
        structure.comment = comment;
        return structure;
    }
}
