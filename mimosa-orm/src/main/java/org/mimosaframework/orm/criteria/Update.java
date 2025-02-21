package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;

/**
 * @author yangankang
 */
public interface Update extends Filter<LogicUpdate> {
    LogicUpdate setTableClass(Class<?> c);

    LogicUpdate nested(WrapsNested nested);

    Update set(Object key, Object value);

    <F> Update set(FieldFunction<F> key, Object value);

    LogicUpdate addSelf(Object key);

    <F> LogicUpdate addSelf(FieldFunction<F> key);

    LogicUpdate subSelf(Object key);

    <F> LogicUpdate subSelf(FieldFunction<F> key);

    LogicUpdate addSelf(Object key, long step);

    <F> LogicUpdate addSelf(FieldFunction<F> key, long step);

    LogicUpdate subSelf(Object key, long step);

    <F> LogicUpdate subSelf(FieldFunction<F> key, long step);

    LogicUpdate addSelf(Object key, String step);

    <F> LogicUpdate addSelf(FieldFunction<F> key, String step);

    LogicUpdate subSelf(Object key, String step);

    <F> LogicUpdate subSelf(FieldFunction<F> key, String step);

    long update();

    Query covert2query();
}
