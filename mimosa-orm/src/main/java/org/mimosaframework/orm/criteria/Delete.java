package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Delete extends Filter<LogicDelete> {
    LogicDelete setTableClass(Class<?> c);

    LogicDelete nested(WrapsNested nested);

    long delete();

    LogicDelete unsafe();

    Query covert2query();
}
