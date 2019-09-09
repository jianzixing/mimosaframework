package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.Paging;

import java.util.List;

/**
 * @author yangankang
 */
public interface Query<T> extends OpFilter<Query>, OpLimit {

    Query addLinked(LogicLinked linked);

    Query andLinked(LogicLinked linked);

    Query orLinked(LogicLinked linked);

    Query and(Filter filter);

    Query or(Filter filter);

    Filter addFilter();

    Query addSubjoin(Join join);

    Join subjoin(Class<?> table);

    Query addOrder(Order order);

    Order order();

    Query addLimit(Limit limit);

    LimitInterface limit();

    List<T> list();

    List<ModelObject> queries();

    ModelObject query();

    boolean hasWhere();

    long count();

    Paging paging();

    T get();

    Query setTableClass(Class c);

    Query from(ModelObject object);

    Query fromBean(Object o);

    @Override
    Query eq(Object key, Object value);

    @Override
    Query in(Object key, Iterable values);

    @Override
    Query in(Object key, Object... values);

    @Override
    Query nin(Object key, Iterable values);

    @Override
    Query nin(Object key, Object... values);

    @Override
    Query like(Object key, Object value);

    @Override
    Query ne(Object key, Object value);

    @Override
    Query gt(Object key, Object value);

    @Override
    Query gte(Object key, Object value);

    @Override
    Query lt(Object key, Object value);

    @Override
    Query lte(Object key, Object value);

    @Override
    Query between(Object key, Object start, Object end);

    @Override
    Query isNull(Object key);

    @Override
    Query isNotNull(Object key);

    Query master();

    Query slave();

    Query slave(String name);

    /**
     * 只查询当前字段值
     * 优先使用当前方法
     *
     * @param fields
     * @return
     */
    Query fields(Object... fields);

    Query fields(Class tableClass, Object... fields);

    Query fields(List fields);

    Query fields(Class tableClass, List fields);

    /**
     * 从映射表中排除当前字段值
     * 优先使用fields方法
     *
     * @param fields
     * @return
     */
    Query excludes(Object... fields);

    Query excludes(Class tableClass, Object... fields);

    Query excludes(List fields);

    Query excludes(Class tableClass, List fields);
}
