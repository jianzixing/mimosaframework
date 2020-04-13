package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

public interface Function extends Filter {
    Function addFunction(BasicFunction function, Object field);

    Function addFunction(BasicFunction function, Object field, String alias);

    Function addFunction(FunctionField function);

    Function master();

    Function slave();

    Function slave(String name);

    Function addLinked(LogicLinked linked);

    Function andLinked(LogicLinked linked);

    Function orLinked(LogicLinked linked);

    Function and(Filter filter);

    Function or(Filter filter);

    Function groupBy(Object field);

    Function orderBy(Object field, boolean isAsc);

    Function childGroupBy(Object field);

    Filter addFilter();

    Query query();

    @Override
    Function eq(Object key, Object value);

    @Override
    Function in(Object key, Iterable values);

    @Override
    Function in(Object key, Object... values);

    @Override
    Function like(Object key, Object value);

    @Override
    Function ne(Object key, Object value);

    @Override
    Function gt(Object key, Object value);

    @Override
    Function gte(Object key, Object value);

    @Override
    Function lt(Object key, Object value);

    @Override
    Function lte(Object key, Object value);

    @Override
    Function between(Object key, Object start, Object end);

    @Override
    Function isNull(Object key);

    @Override
    Function isNotNull(Object key);

    Class getTableClass();
}
