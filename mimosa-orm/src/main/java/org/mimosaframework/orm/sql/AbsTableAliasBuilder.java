package org.mimosaframework.orm.sql;

public interface AbsTableAliasBuilder<T> extends AbsTableBuilder<T> {
    T table(Class table, String tableAliasName);
}
