package org.mimosaframework.orm.criteria;

public interface LogicFilter<Q, T> extends QueryFilter<T> {
    Q or();

    Q and();

    T nested(WrapsNested nested);
}
