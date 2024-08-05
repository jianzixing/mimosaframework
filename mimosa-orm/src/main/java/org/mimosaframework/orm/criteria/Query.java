package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.orm.Paging;

import java.util.List;

/**
 * @author yangankang
 */
public interface Query extends QueryFilter<LogicQuery> {

    LogicQuery filter(DefaultFilter as);

    LogicQuery linked(WrapsLinked linked);

    LogicQuery subjoin(Join join);

    LogicQuery orderBy(OrderBy order);

    LogicQuery orderBy(Object field, boolean isAsc);

    <F> LogicQuery orderBy(FieldFunction<F> field, boolean isAsc);

    LogicQuery orderBy(Object field, Sort sort);

    <F> LogicQuery orderBy(FieldFunction<F> field, Sort sort);

    LogicQuery withoutOrderBy();

    LogicQuery limit(Limit limit);

    LogicQuery limit(long start, long limit);

    LogicQuery setTableClass(Class c);

    LogicQuery forUpdate();

    LogicQuery forUpdate(boolean is);

    LogicQuery master();

    LogicQuery slave();

    LogicQuery slave(String name);

    /**
     * 只查询当前字段值
     * 优先使用当前方法
     *
     * @param fields
     * @return
     */
    LogicQuery fields(Object... fields);

    <F> LogicQuery fields(FieldFunction<F>... fields);

    LogicQuery fields(Class tableClass, Object... fields);

    <F> LogicQuery fields(Class tableClass, FieldFunction<F>... fields);

    LogicQuery fields(List<Object> fields);

    LogicQuery fields(Class tableClass, List<Object> fields);

    /**
     * 从映射表中排除当前字段值
     * 优先使用fields方法
     *
     * @param fields
     * @return
     */
    LogicQuery excludes(Object... fields);

    <F> LogicQuery excludes(FieldFunction<F>... fields);

    LogicQuery excludes(Class tableClass, Object... fields);

    <F> LogicQuery excludes(Class tableClass, FieldFunction<F>... fields);

    LogicQuery excludes(List<Object> fields);

    LogicQuery excludes(Class tableClass, List<Object> fields);

    Class getTableClass();

    Paging paging();

    <M> M get();

    <M> List<M> list();

    long count();

    LogicQuery as(String as);
}
