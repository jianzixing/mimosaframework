package org.mimosaframework.orm.sql;

public interface GroupByBuilder<T> extends UnifyBuilder {
    T groupBy();
}
