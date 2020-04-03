package org.mimosaframework.orm.sql.delete;

public interface DeleteWhichBuilder<T> {
    T table(Class table);

    T table(String aliasName);
}
