package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.orm.Paging;

import java.util.List;

/**
 * @author yangankang
 */
public interface Query<T> extends QueryFilter<LogicQuery<T>> {

    LogicQuery<T> filter(DefaultFilter as);

    LogicQuery<T> nested(WrapsNested nested);

    LogicQuery<T> subjoin(Join join);

    LogicQuery<T> orderBy(OrderBy order);

    LogicQuery<T> orderBy(Object field, boolean isAsc);

    <F> LogicQuery<T> orderBy(FieldFunction<F> field, boolean isAsc);

    LogicQuery<T> orderBy(Object field, Sort sort);

    <F> LogicQuery<T> orderBy(FieldFunction<F> field, Sort sort);

    LogicQuery<T> withoutOrderBy();

    LogicQuery<T> limit(Limit limit);

    LogicQuery<T> limit(long start, long limit);

    LogicQuery<T> setTableClass(Class c);

    LogicQuery<T> forUpdate();

    LogicQuery<T> forUpdate(boolean is);

    LogicQuery<T> master();

    LogicQuery<T> slave();

    LogicQuery<T> slave(String name);

    /**
     * 只查询当前字段值
     * 优先使用当前方法
     *
     * @param fields
     * @return
     */
    LogicQuery<T> fields(Object... fields);

    <F> LogicQuery<T> fields(FieldFunction<F>... fields);

    LogicQuery<T> fields(Class tableClass, Object... fields);

    <F> LogicQuery<T> fields(Class tableClass, FieldFunction<F>... fields);

    LogicQuery<T> fields(List<Object> fields);

    LogicQuery<T> fields(Class tableClass, List<Object> fields);

    /**
     * 从映射表中排除当前字段值
     * 优先使用fields方法
     *
     * @param fields
     * @return
     */
    LogicQuery<T> excludes(Object... fields);

    <F> LogicQuery<T> excludes(FieldFunction<F>... fields);

    LogicQuery<T> excludes(Class tableClass, Object... fields);

    <F> LogicQuery<T> excludes(Class tableClass, FieldFunction<F>... fields);

    LogicQuery<T> excludes(List<Object> fields);

    LogicQuery<T> excludes(Class tableClass, List<Object> fields);

    Class getTableClass();

    Paging<T> paging();

    T get();

    List<T> list();

    long count();

    LogicQuery<T> as(String as);
}
