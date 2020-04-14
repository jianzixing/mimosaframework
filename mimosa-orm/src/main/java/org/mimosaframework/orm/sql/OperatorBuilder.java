package org.mimosaframework.orm.sql;

public interface OperatorBuilder<T, S> extends OperatorEqualBuilder<T> {
    T in();

    T nin();

    T like();

    T ne();

    T gt();

    T gte();

    T lt();

    T lte();

    BetweenValueBuilder<S> notBetween();

    BetweenValueBuilder<S> between();
}
