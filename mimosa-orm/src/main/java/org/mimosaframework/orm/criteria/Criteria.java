package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public final class Criteria {

    public static final Query<LogicQuery> query(Class c) {
        return new DefaultQuery(c);
    }

    public static final Filter filter() {
        return new DefaultFilter();
    }

    public static final Join join(Class c) {
        return new DefaultJoin(c);
    }

    public static final Join join(Class main, Class join) {
        return new DefaultJoin(main, join);
    }

    public static final Update update(Class c) {
        return new DefaultUpdate(c);
    }

    public static final Delete<LogicDelete> delete(Class c) {
        return new DefaultDelete(c);
    }

    public static final Function fun(Class c) {
        return new DefaultFunction(c);
    }
}
