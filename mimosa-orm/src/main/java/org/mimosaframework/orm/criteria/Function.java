package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.orm.BasicFunction;

public interface Function<T extends Function> extends Filter<T> {
    T addFunction(BasicFunction function, Object field);

    <F> T addFunction(BasicFunction function, FieldFunction<F> field);

    T addFunction(BasicFunction function, Object field, String alias);

    <F> T addFunction(BasicFunction function, FieldFunction<F> field, String alias);

    T addFunction(FunctionField function);

    T master();

    T slave();

    T slave(String name);

    T nested(WrapsNested nested);

    T groupBy(Object field);

    <F> T groupBy(FieldFunction<F> field);

    T orderBy(Object field, boolean isAsc);

    <F> T orderBy(FieldFunction<F> field, boolean isAsc);

    T orderBy(Object field, Sort sort);

    <F> T orderBy(FieldFunction<F> field, Sort sort);

    T having(HavingField field);

    Query covert2query();

    Class getTableClass();
}
