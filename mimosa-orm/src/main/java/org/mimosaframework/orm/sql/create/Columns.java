package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class Columns {
    public static ColumnTypeBuilder<CreateColumnAssistBuilder> column(String fieldName) {
        return SQLActionFactory.column().column(fieldName);
    }
}
