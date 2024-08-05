package org.mimosaframework.orm.criteria;

public interface WrapsLinked extends Filter<LogicWrapsLinked> {
    LogicWrapsLinked linked(WrapsLinked linked);

    Wraps<Filter> getLogicWraps();
}
