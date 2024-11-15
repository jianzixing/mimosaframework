package org.mimosaframework.orm.criteria;

public class DefaultWrapsLinked extends AbstractFilter<LogicWrapsLinked> implements LogicWrapsLinked {
    private Wraps<Filter> logicWraps;

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public WrapsLinked and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    public WrapsLinked or() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.OR);
        }
        return this;
    }

    public LogicWrapsLinked linked(WrapsLinked linked) {
        Wraps lw = ((DefaultWrapsLinked) linked).logicWraps;

        if (logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
    }

    private void addFilterInLinked(Filter filter) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }
        this.logicWraps.addLast(new WrapsObject<Filter>(filter));
    }

    @Override
    public LogicWrapsLinked eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked exists(LogicQuery query) {
        // 之后需要实现
        return this;
    }
}
