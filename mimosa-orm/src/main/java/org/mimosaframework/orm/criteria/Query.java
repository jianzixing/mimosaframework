package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.orm.Paging;

import java.util.List;

/**
 * @author yangankang
 */
public interface Query<T extends Query> extends QueryFilter<T> {

    T filter(DefaultFilter as);

    T linked(WrapsLinked linked);

    T subjoin(Join join);

    T orderBy(OrderBy order);

    T orderBy(Object field, boolean isAsc);

    <F> T orderBy(FieldFunction<F> field, boolean isAsc);

    T orderBy(Object field, Sort sort);

    <F> T orderBy(FieldFunction<F> field, Sort sort);

    T withoutOrderBy();

    T limit(Limit limit);

    T limit(long start, long limit);

    T setTableClass(Class c);

    T forUpdate();

    T forUpdate(boolean is);

    T master();

    T slave();

    T slave(String name);

    /**
     * 只查询当前字段值
     * 优先使用当前方法
     *
     * @param fields
     * @return
     */
    T fields(Object... fields);

    <F> T fields(FieldFunction<F>... fields);

    T fields(Class tableClass, Object... fields);

    <F> T fields(Class tableClass, FieldFunction<F>... fields);

    T fields(List<Object> fields);

    T fields(Class tableClass, List<Object> fields);

    /**
     * 从映射表中排除当前字段值
     * 优先使用fields方法
     *
     * @param fields
     * @return
     */
    T excludes(Object... fields);

    <F> T excludes(FieldFunction<F>... fields);

    T excludes(Class tableClass, Object... fields);

    <F> T excludes(Class tableClass, FieldFunction<F>... fields);

    T excludes(List<Object> fields);

    T excludes(Class tableClass, List<Object> fields);

    Class getTableClass();

    Paging paging();

    <M> M get();

    <M> List<M> list();

    long count();

    T as(String as);
}
