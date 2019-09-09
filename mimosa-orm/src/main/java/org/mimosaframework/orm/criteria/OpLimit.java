package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface OpLimit {
    Query limit(long start, long count);

    Query order(Object field, boolean isAsc);

    Query order(Class tableClass, Object field, boolean isAsc);
}
