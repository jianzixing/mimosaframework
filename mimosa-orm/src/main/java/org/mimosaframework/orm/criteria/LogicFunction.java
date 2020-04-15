package org.mimosaframework.orm.criteria;

public interface LogicFunction extends Function<LogicFunction> {
    Function<LogicFunction> and();

    Function<LogicFunction> or();
}
