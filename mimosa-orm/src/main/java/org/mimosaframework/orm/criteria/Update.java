package org.mimosaframework.orm.criteria;

import java.io.Serializable;

/**
 * @author yangankang
 */
public interface Update<T extends Update> extends Filter<T> {
    T setTableClass(Class c);

    T linked(WrapsLinked linked);

    Update<LogicUpdate> set(Serializable key, Object value);

    T addSelf(Serializable key);

    T subSelf(Serializable key);

    T addSelf(Serializable key, Integer step);

    T subSelf(Serializable key, Integer step);

    T addSelf(Serializable key, String step);

    T subSelf(Serializable key, String step);

    long update();

    Query covert2query();
}
