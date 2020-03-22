package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AlterBuilder;

public class AlterFactory {
    public static AlterAnyBuilder alter() {
        AlterBuilder<AlterAnyBuilder> alterBuilder = null;
        return alterBuilder.alter();
    }
}
