package org.mimosaframework.orm.criteria;

public interface LogicWrapsLinked extends WrapsLinked {
    WrapsLinked<LogicWrapsLinked> and();

    WrapsLinked<LogicWrapsLinked> or();
}
