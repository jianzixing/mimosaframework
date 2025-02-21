package org.mimosaframework.orm.criteria;

public interface LogicWrapsNested extends WrapsNested, LogicFilter<WrapsNested, LogicWrapsNested> {
    WrapsNested and();

    WrapsNested or();
}
