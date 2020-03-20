package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.UpdateBuilder;

public class UpdateFactory {
    public static UpdateStartBuilder update() {
        UpdateBuilder<UpdateStartBuilder> updateBuilder = null;
        return updateBuilder.update();
    }
}
