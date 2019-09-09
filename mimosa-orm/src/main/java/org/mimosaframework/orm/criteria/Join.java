package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Join {

    Query query();

    Join parent();

    Join setQuery(Query query);

    Join addChildJoin(Join join);

    Join childJoin(Class<?> table);

    /**
     * 添加过滤条件，不是on条件
     *
     * @param filter
     * @return
     */
    Join add(Filter filter);

    /**
     * 添加过滤条件，不是on条件
     *
     * @return
     */
    Filter filter();

    Join join(Object self, Object value);

    Join eq(Object self, Object mainField);

    Join ne(Object self, Object mainField);

    Join gt(Object self, Object mainField);

    Join gte(Object self, Object mainField);

    Join lt(Object self, Object mainField);

    Join lte(Object self, Object mainField);

    Join aliasName(Object s);

    Join single();

    Join setMulti(boolean is);
}
