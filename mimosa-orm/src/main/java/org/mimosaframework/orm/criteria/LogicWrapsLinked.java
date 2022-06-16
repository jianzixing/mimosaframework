package org.mimosaframework.orm.criteria;

public interface LogicWrapsLinked extends WrapsLinked<LogicWrapsLinked>, LogicFilter<WrapsLinked<LogicWrapsLinked>, LogicWrapsLinked> {
    WrapsLinked<LogicWrapsLinked> and();

    WrapsLinked<LogicWrapsLinked> or();
}
