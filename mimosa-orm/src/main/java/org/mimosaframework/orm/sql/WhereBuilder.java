package org.mimosaframework.orm.sql;

public interface WhereBuilder<T> extends UnifyBuilder {
    T where();
}
