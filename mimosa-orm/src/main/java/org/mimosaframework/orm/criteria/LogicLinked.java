package org.mimosaframework.orm.criteria;

public class LogicLinked implements Filter {
    private LogicWraps<Filter> logicWraps;

    public LogicWraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public LogicLinked and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    public LogicLinked or() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.OR);
        }
        return this;
    }

    public LogicLinked linked(LogicLinked linked) {
        LogicWraps lw = linked.logicWraps;

        if (logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
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
