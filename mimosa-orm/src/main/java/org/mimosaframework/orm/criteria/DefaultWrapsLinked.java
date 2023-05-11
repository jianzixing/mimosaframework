package org.mimosaframework.orm.criteria;

import java.io.Serializable;

public class DefaultWrapsLinked implements LogicWrapsLinked {
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
    public LogicWrapsLinked eq(Serializable key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked in(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked in(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked nin(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked nin(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked like(Serializable key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked ne(Serializable key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked gt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked gte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked lt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked lte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked between(Serializable key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked isNull(Serializable key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked isNotNull(Serializable key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInLinked(filter);
        return this;
    }

    @Override
    public LogicWrapsLinked exists(Query query) {
        // 之后需要实现
        return this;
    }
}
