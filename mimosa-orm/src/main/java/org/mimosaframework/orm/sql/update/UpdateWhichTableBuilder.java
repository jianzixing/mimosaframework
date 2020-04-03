package org.mimosaframework.orm.sql.update;

public interface UpdateWhichTableBuilder<T> {
    T table(Class table);

    T table(Class table, String aliasName);
}
