package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;

/**
 * @author yangankang
 */
public interface Update<T extends Update> extends Filter<T> {
    T setTableClass(Class c);

    T linked(WrapsLinked linked);

    Update<LogicUpdate> set(Object key, Object value);

    <F> Update<LogicUpdate> set(FieldFunction<F> key, Object value);

    T addSelf(Object key);

    <F> T addSelf(FieldFunction<F> key);

    T subSelf(Object key);

    <F> T subSelf(FieldFunction<F> key);

    T addSelf(Object key, long step);

    <F> T addSelf(FieldFunction<F> key, long step);

    T subSelf(Object key, long step);

    <F> T subSelf(FieldFunction<F> key, long step);

    T addSelf(Object key, String step);

    <F> T addSelf(FieldFunction<F> key, String step);

    T subSelf(Object key, String step);

    <F> T subSelf(FieldFunction<F> key, String step);

    long update();

    Query covert2query();
}
