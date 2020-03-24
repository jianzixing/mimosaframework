package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.SQLActionFactory;

import java.io.Serializable;

public class Columns {
    public static ColumnTypeBuilder<CreateColumnAssistBuilder> column(Serializable fieldName) {
        return SQLActionFactory.column().column(fieldName);
    }
}
