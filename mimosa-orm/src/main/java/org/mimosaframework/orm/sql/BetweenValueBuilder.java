package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface BetweenValueBuilder<T> {
    T section(Serializable valueA, Serializable valueB);
}
