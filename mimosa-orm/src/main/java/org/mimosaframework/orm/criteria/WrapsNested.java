package org.mimosaframework.orm.criteria;

public interface WrapsNested extends Filter<LogicWrapsNested> {
    LogicWrapsNested nested(WrapsNested nested);

    Wraps<Filter> getLogicWraps();
}
