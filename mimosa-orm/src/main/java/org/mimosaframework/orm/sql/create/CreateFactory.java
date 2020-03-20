package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.CreateBuilder;

public class CreateFactory {
    public static CreateAnyBuilder create() {
        CreateBuilder<CreateAnyBuilder> createBuilder = null;
        return createBuilder.create();
    }
}
