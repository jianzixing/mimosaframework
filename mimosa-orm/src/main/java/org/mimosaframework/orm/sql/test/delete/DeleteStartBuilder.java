package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.DeleteTableBuilder;
import org.mimosaframework.orm.sql.test.FromBuilder;
import org.mimosaframework.orm.sql.test.TableBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface DeleteStartBuilder
        extends DeleteTableBuilder<FromBuilder<TableBuilder<WhereBuilder>>>,
        FromBuilder<TableBuilder<WhereBuilder>> {
}
