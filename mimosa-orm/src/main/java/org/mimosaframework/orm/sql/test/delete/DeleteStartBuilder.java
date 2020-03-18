package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.*;

public interface DeleteStartBuilder
        extends
        BeforeDeleteFromBuilder
                <FromBuilder
                        <AbsTablesBuilder
                                <WhereBuilder<DeleteWhereBuilder>>>>,

        FromBuilder
                <AbsTablesBuilder
                        <ReplaceDeleteWhereBuilder>> {
}
