package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public class SelectFactory {
    public static FieldBuilder<FromBuilder<TableBuilder<WhereBuilder>>> select() {
        SelectBuilder<FieldBuilder> selectBuilder = null;
        return selectBuilder.select();
    }
}
