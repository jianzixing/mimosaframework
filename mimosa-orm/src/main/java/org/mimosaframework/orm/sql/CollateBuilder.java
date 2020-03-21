package org.mimosaframework.orm.sql;

public interface CollateBuilder<T> extends UnifyBuilder {
    T collate(String collate);
}
