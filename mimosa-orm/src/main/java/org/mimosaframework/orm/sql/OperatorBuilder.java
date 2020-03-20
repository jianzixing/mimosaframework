package org.mimosaframework.orm.sql;

public interface OperatorBuilder<T> extends OperatorEqualBuilder<T> {
    T in();

    T nin();

    T like();

    T ne();

    T gt();

    T gte();

    T lt();

    T lte();

    BetweenValueBuilder<T> between();
}
