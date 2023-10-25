package org.mimosaframework.orm.criteria;

import java.io.Serializable;

/**
 * @author yangankang
 */
public final class Criteria {

    public static Query<LogicQuery> query(Class c) {
        return new DefaultQuery(c);
    }

    public static LogicQuery logicQuery(Class c) {
        return new DefaultQuery(c);
    }

    public static LogicDelete logicDelete(Class c) {
        return new DefaultDelete(c);
    }

    public static LogicUpdate logicDUpdate(Class c) {
        return new DefaultUpdate(c);
    }

    public static DefaultFilter filter() {
        return new DefaultFilter();
    }

    public static Join join(Class c) {
        return new DefaultJoin(c, 0);
    }

    public static Join left(Class c) {
        return new DefaultJoin(c, 0);
    }

    public static Join inner(Class c) {
        return new DefaultJoin(c, 1);
    }

    public static Update<LogicUpdate> update(Class c) {
        return new DefaultUpdate(c);
    }

    public static Delete<LogicDelete> delete(Class c) {
        return new DefaultDelete(c);
    }

    public static Function<LogicFunction> fun(Class c) {
        return new DefaultFunction(c);
    }

    public static WrapsLinked<LogicWrapsLinked> linked() {
        return new DefaultWrapsLinked();
    }

    public static LogicWrapsLinked logicLinked() {
        return new DefaultWrapsLinked();
    }

    public static LogicWrapsLinked linked(WrapsLinked<LogicWrapsLinked> l) {
        DefaultWrapsLinked linked = new DefaultWrapsLinked();
        linked.linked(l);
        return linked;
    }

    public static <T> UpdateObject wrapUpdate(T t) {
        return new UpdateObject(t);
    }

    public static AsField as(String alias, Serializable field) {
        return new AsField(alias, field);
    }

    public static Keyword NULL() {
        return Keyword.NULL;
    }
}
