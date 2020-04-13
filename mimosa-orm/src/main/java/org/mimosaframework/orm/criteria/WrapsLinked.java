package org.mimosaframework.orm.criteria;

public interface WrapsLinked<T extends WrapsLinked> extends Filter<T> {
    T linked(WrapsLinked linked);

    Wraps<Filter> getLogicWraps();
}
