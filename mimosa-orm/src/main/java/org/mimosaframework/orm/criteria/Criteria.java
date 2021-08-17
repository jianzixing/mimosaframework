package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public final class Criteria {

    public static final Query<LogicQuery> query(Class c) {
        return new DefaultQuery(c);
    }

    public static final LogicQuery logicQuery(Class c) {
        return new DefaultQuery(c);
    }

    public static final DefaultFilter filter() {
        return new DefaultFilter();
    }

    public static final Join join(Class c) {
        return new DefaultJoin(c, 0);
    }

    public static final Join left(Class c) {
        return new DefaultJoin(c, 0);
    }

    public static final Join inner(Class c) {
        return new DefaultJoin(c, 1);
    }

    public static final Update<LogicUpdate> update(Class c) {
        return new DefaultUpdate(c);
    }

    public static final Delete<LogicDelete> delete(Class c) {
        return new DefaultDelete(c);
    }

    public static final Function<LogicFunction> fun(Class c) {
        return new DefaultFunction(c);
    }

    public static final WrapsLinked<LogicWrapsLinked> linked() {
        return new DefaultWrapsLinked();
    }

    public static final LogicWrapsLinked linked(WrapsLinked<LogicWrapsLinked> l) {
        DefaultWrapsLinked linked = new DefaultWrapsLinked();
        linked.linked(l);
        return linked;
    }

    public static final <T> UpdateObject wrapUpdate(T t) {
        return new UpdateObject(t);
    }
}
