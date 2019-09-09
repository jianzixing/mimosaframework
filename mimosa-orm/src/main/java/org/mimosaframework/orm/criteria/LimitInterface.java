package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface LimitInterface {
    Query goQuery();

    LimitInterface limit(long start, long count);
}
