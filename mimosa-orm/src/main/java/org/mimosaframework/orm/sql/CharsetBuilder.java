package org.mimosaframework.orm.sql;

public interface CharsetBuilder<T> extends UnifyBuilder {
    T charset(String charset);
}
