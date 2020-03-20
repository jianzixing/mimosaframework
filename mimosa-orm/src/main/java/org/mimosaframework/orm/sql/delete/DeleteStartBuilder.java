package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTablesBuilder;
import org.mimosaframework.orm.sql.FromBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

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
