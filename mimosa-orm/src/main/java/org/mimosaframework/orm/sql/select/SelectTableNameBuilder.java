package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface SelectTableNameBuilder<T> extends AbsTableAliasBuilder<T> {
    T table(String tableName);

    T table(String tableName, String tableAliasName);

    T table(UnifyBuilder builder);

    T table(UnifyBuilder builder, String tableAliasName);
}
