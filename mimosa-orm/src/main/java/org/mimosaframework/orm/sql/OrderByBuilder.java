package org.mimosaframework.orm.sql;

public interface OrderByBuilder<T> extends UnifyBuilder{
    T orderBy();
}
