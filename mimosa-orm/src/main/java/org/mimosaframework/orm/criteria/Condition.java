package org.mimosaframework.orm.criteria;

@FunctionalInterface
public interface Condition<T> {
    boolean criteria(Filter<T> filter);
}
