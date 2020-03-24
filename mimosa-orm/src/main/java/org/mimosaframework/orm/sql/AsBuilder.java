package org.mimosaframework.orm.sql;

public interface AsBuilder<T> {
    T as(String tableAliasName);
}
