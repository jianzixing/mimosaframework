package org.mimosaframework.orm.sql;

public interface BetweenValueBuilder<T> {
    T section(Object valueA, Object valueB);
}
