package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.FromBuilder;
import org.mimosaframework.orm.sql.test.TableBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface DeleteStartBuilder
        extends BeforeDeleteFromBuilder<FromBuilder<TableBuilder<WhereBuilder>>>,
        FromBuilder<TableBuilder<AfterDeleteFromBuilder<WhereBuilder>>> {
}
