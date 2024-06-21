package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public interface Update<T extends Update> extends Filter<T> {
    T setTableClass(Class c);

    T linked(WrapsLinked linked);

    Update<LogicUpdate> set(Object key, Object value);

    T addSelf(Object key);

    T subSelf(Object key);

    T addSelf(Object key, long step);

    T subSelf(Object key, long step);

    T addSelf(Object key, String step);

    T subSelf(Object key, String step);

    long update();

    Query covert2query();
}
