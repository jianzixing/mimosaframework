package org.mimosaframework.orm.criteria;

public class DefaultWrapsNested extends AbstractFilter<LogicWrapsNested> implements LogicWrapsNested {
    private Wraps<Filter> logicWraps;

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public WrapsNested and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    public WrapsNested or() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.OR);
        }
        return this;
    }

    public LogicWrapsNested nested(WrapsNested nested) {
        Wraps lw = ((DefaultWrapsNested) nested).logicWraps;

        if (logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
    }

    private void addFilterInNested(Filter filter) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }
        this.logicWraps.addLast(new WrapsObject<Filter>(filter));
    }

    @Override
    public LogicWrapsNested eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicWrapsNested exists(LogicQuery query) {
        // 之后需要实现
        return this;
    }
}
