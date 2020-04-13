package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

public interface Function extends Filter<Function> {
    Function addFunction(BasicFunction function, Object field);

    Function addFunction(BasicFunction function, Object field, String alias);

    Function addFunction(FunctionField function);

    Function master();

    Function slave();

    Function slave(String name);

    Function linked(LogicLinked linked);

    Function and();

    Function or();

    Function groupBy(Object field);

    Function orderBy(Object field, boolean isAsc);

    Function childGroupBy(Object field);

    Query covert2query();

    Class getTableClass();
}
