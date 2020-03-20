package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.SelectBuilder;

public class SelectFactory {
    public static SelectStartBuilder select() {
        SelectBuilder<SelectStartBuilder> selectBuilder = null;
        return selectBuilder.select();
    }
}
