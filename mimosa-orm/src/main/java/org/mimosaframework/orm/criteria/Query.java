package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.Paging;

import java.util.List;

/**
 * @author yangankang
 */
public interface Query<T extends Query> extends Filter<T> {

    T linked(WrapsLinked linked);

    T subjoin(Join join);

    T order(Order order);

    T order(Object field, boolean isAsc);

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

    T fields(Class tableClass, Object... fields);

    T fields(List fields);

    T fields(Class tableClass, List fields);

    /**
     * 从映射表中排除当前字段值
     * 优先使用fields方法
     *
     * @param fields
     * @return
     */
    T excludes(Object... fields);

    T excludes(Class tableClass, Object... fields);

    T excludes(List fields);

    T excludes(Class tableClass, List fields);

    Class getTableClass();

    Paging paging();

    <M> M get();

    <M> List<M> list();

    long count();
}
