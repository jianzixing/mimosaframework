package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Delete<T extends Delete> extends Filter<T> {
    T setTableClass(Class c);

    T linked(WrapsLinked linked);

    Query covert2query();
}
