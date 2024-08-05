package org.mimosaframework.orm.criteria;

public interface LogicWrapsLinked extends WrapsLinked, LogicFilter<WrapsLinked, LogicWrapsLinked> {
    WrapsLinked and();

    WrapsLinked or();
}
