package org.mimosaframework.orm.sql;

public interface AbsExtraBuilder<T> extends UnifyBuilder {
    T extra(String sql);
}
