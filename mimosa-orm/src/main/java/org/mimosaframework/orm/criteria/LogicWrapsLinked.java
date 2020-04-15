package org.mimosaframework.orm.criteria;

public interface LogicWrapsLinked extends WrapsLinked<LogicWrapsLinked> {
    WrapsLinked<LogicWrapsLinked> and();

    WrapsLinked<LogicWrapsLinked> or();
}
