package org.mimosaframework.orm.criteria;

@FunctionalInterface
public interface Condition<T> {
    void criteria(Filter<T> filter);
}
