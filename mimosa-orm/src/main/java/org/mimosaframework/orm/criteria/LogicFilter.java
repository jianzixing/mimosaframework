package org.mimosaframework.orm.criteria;

public interface LogicFilter<Q, T> extends Filter<T> {
    Q or();

    Q and();
}
