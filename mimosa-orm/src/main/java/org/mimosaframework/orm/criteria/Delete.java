package org.mimosaframework.orm.criteria;

import java.util.List;

/**
 * @author yangankang
 */
public interface Delete extends OpFilter<Delete> {
    Delete setTableClass(Class c);

    Delete addLinked(LogicLinked linked);

    Delete andLinked(LogicLinked linked);

    Delete orLinked(LogicLinked linked);

    Delete and(Filter filter);

    Delete or(Filter filter);

    Filter addFilter();

    void delete();

    Query query();

    @Override
    Delete eq(Object key, Object value);

    @Override
    Delete in(Object key, Iterable values);

    @Override
    Delete in(Object key, Object... values);

    @Override
    Delete like(Object key, Object value);

    @Override
    Delete ne(Object key, Object value);

    @Override
    Delete gt(Object key, Object value);

    @Override
    Delete gte(Object key, Object value);

    @Override
    Delete lt(Object key, Object value);

    @Override
    Delete lte(Object key, Object value);

    @Override
    Delete between(Object key, Object start, Object end);

    @Override
    Delete isNull(Object key);

    @Override
    Delete isNotNull(Object key);
}
