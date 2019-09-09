package org.mimosaframework.orm.criteria;

import java.util.List;

public class LogicLinked implements Filter {
    private LogicWraps<Filter> logicWraps;

    public static final LogicLinked getInstance() {
        return new LogicLinked();
    }

    public static final LogicLinked getInstance(Filter filter) {
        LogicLinked logicLinked = new LogicLinked();
        logicLinked.and(filter);
        return logicLinked;
    }

    public LogicWraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public LogicLinked and(Filter filter) {
        if (filter != null) {
            if (logicWraps == null) {
                this.logicWraps = new LogicWraps<>();
            }
            this.logicWraps.addLast(new LogicWrapObject(filter), CriteriaLogic.AND);
        }
        return this;
    }

    public LogicLinked wrap(LogicLinked linked) {
        return this.andwrap(linked);
    }

    public LogicLinked or(Filter filter) {
        if (filter != null) {
            if (logicWraps == null) {
                this.logicWraps = new LogicWraps<>();
            }
            this.logicWraps.addLast(new LogicWrapObject(filter), CriteriaLogic.OR);
        }
        return this;
    }

    public LogicLinked andwrap(LogicLinked linked) {
        LogicWraps lw = linked.logicWraps;

        if (logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
    }

    public LogicLinked orwrap(LogicLinked linked) {
        LogicWraps lw = linked.logicWraps;
        if (logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw, CriteriaLogic.OR);
        return this;
    }

    @Override
    public Query query() {
        return null;
    }

    @Override
    public Join join() {
        return null;
    }

    @Override
    public Update update() {
        return null;
    }

    @Override
    public Delete delete() {
        return null;
    }

    private void addFilterInLinked(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLast(new LogicWrapObject<Filter>(filter), logic);
    }

    @Override
    public LogicLinked eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Filter nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Filter nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicLinked isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }
}
