package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.*;

public interface DeleteStartBuilder
        extends
        BeforeDeleteFromBuilder
                <FromBuilder
                        <TablesBuilder
                                <WhereBuilder<DeleteWhereBuilder>>>>,

        FromBuilder
                <TablesBuilder
                        <ReplaceDeleteWhereBuilder>> {
}
