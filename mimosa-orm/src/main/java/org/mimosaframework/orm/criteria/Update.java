package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Update extends OpFilter<Update> {
    Update setTableClass(Class c);

    Update addLinked(LogicLinked linked);

    Update andLinked(LogicLinked linked);

    Update orLinked(LogicLinked linked);

    Update and(Filter filter);

    Update or(Filter filter);

    Filter addFilter();

    Update add(Object key, Object value);

    Update value(Object key, Object value);

    Update addSelf(Object key);

    Update subSelf(Object key);

    Update addSelf(Object key, Integer step);

    Update subSelf(Object key, Integer step);

    Update addSelf(Object key, String step);

    Update subSelf(Object key, String step);

    void update();

    Query query();

    @Override
    Update eq(Object key, Object value);

    @Override
    Update in(Object key, Iterable values);

    @Override
    Update in(Object key, Object... values);

    @Override
    Update like(Object key, Object value);

    @Override
    Update ne(Object key, Object value);

    @Override
    Update gt(Object key, Object value);

    @Override
    Update gte(Object key, Object value);

    @Override
    Update lt(Object key, Object value);

    @Override
    Update lte(Object key, Object value);

    @Override
    Update between(Object key, Object start, Object end);

    @Override
    Update isNull(Object key);

    @Override
    Update isNotNull(Object key);
}
