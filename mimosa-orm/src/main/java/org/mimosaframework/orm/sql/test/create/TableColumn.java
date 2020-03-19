package org.mimosaframework.orm.sql.test.create;

public class TableColumn {
    private String fieldName;

    public TableColumn(String fieldName) {
        this.fieldName = fieldName;
    }

    public static ColumnTypeBuilder<CreateColumnAssistBuilder> name(String fieldName) {
        return null;
    }
}
