package org.mimosaframework.orm.sql;

public interface SortBuilder<T> {
    T asc();

    T desc();
}
