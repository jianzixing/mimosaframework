package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Update<T extends Update> extends Filter<T> {
    T setTableClass(Class c);

    T linked(WrapsLinked linked);

    T set(Object key, Object value);

    T addSelf(Object key);

    T subSelf(Object key);

    T addSelf(Object key, Integer step);

    T subSelf(Object key, Integer step);

    T addSelf(Object key, String step);

    T subSelf(Object key, String step);

    Query covert2query();
}
