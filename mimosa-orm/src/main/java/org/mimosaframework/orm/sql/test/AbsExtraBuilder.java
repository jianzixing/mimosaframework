package org.mimosaframework.orm.sql.test;

public interface AbsExtraBuilder<T> {
    T extra(String sql);
}
