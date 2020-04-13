package org.mimosaframework.orm.criteria;

import java.util.List;

/**
 * @author yangankang
 */
public interface Query extends Filter<Query> {

    Query linked(LogicLinked linked);

    Query subjoin(Join join);

    Query order(Order order);

    Query order(Object field, boolean isAsc);

    Query order(Class tableClass, Object field, boolean isAsc);

    Query limit(Limit limit);

    Query limit(long start, long limit);

    Query setTableClass(Class c);

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

    Class getTableClass();
}
