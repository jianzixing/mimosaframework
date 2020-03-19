package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.CreateBuilder;

public class CreateFactory {
    public static CreateAnyBuilder create() {
        CreateBuilder<CreateAnyBuilder> createBuilder = null;
        return createBuilder.create();
    }

    public static ColumnTypeBuilder<CreateColumnAssistBuilder> column(String fieldName) {
        return null;
    }
}
