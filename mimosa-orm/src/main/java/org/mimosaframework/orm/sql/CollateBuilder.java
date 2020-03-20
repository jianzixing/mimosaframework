package org.mimosaframework.orm.sql;

public interface CollateBuilder<T> {
    T collate(String collate);
}
