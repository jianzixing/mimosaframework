package org.mimosaframework.orm.criteria;

public interface LogicFunction extends Function {
    Function<LogicFunction> and();

    Function<LogicFunction> or();
}
