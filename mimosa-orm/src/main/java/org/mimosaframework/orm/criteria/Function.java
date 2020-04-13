package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

public interface Function<T extends Function> extends Filter<T> {
    T addFunction(BasicFunction function, Object field);

    T addFunction(BasicFunction function, Object field, String alias);

    T addFunction(FunctionField function);

    T master();

    T slave();

    T slave(String name);

    T linked(WrapsLinked linked);

    T groupBy(Object field);

    T orderBy(Object field, boolean isAsc);

    T childGroupBy(Object field);

    Query covert2query();

    Class getTableClass();
}
