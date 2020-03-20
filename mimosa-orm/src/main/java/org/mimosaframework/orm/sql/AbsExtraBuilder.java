package org.mimosaframework.orm.sql;

public interface AbsExtraBuilder<T> {
    T extra(String sql);
}
