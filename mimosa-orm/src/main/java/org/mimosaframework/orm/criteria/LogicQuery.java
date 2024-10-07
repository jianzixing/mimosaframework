package org.mimosaframework.orm.criteria;

public interface LogicQuery<T> extends Query<T>, LogicFilter<Query<T>, LogicQuery<T>> {
    Query<T> and();

    Query<T> or();
}
