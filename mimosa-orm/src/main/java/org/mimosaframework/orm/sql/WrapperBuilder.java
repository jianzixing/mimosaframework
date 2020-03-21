package org.mimosaframework.orm.sql;

public interface WrapperBuilder<T> {
    T wrapper(LogicBuilder wrapper);
}
