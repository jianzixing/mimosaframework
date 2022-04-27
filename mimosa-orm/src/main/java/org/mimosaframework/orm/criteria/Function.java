package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

import java.io.Serializable;

public interface Function<T extends Function> extends Filter<T> {
    T addFunction(BasicFunction function, Serializable field);

    T addFunction(BasicFunction function, Serializable field, String alias);

    T addFunction(FunctionField function);

    T master();

    T slave();

    T slave(String name);

    T linked(WrapsLinked linked);

    T groupBy(Object field);

    T orderBy(Object field, boolean isAsc);

    T having(HavingField field);

    Query covert2query();

    Class getTableClass();
}
