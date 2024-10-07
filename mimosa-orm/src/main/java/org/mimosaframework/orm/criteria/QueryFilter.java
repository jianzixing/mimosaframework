package org.mimosaframework.orm.criteria;

public interface QueryFilter<T> extends Filter<T> {
    T exists(LogicQuery<?> query);
}
