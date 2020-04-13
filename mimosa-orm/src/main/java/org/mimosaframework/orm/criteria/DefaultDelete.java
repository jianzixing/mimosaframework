package org.mimosaframework.orm.criteria;

/**
 * @author yangankang
 */
public class DefaultDelete implements LogicDelete {
    private Wraps<Filter> logicWraps;
    private Class tableClass;

    public DefaultDelete(Class tableClass) {
        this.tableClass = tableClass;
    }

    public Class getTableClass() {
        return tableClass;
    }

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public void setLogicWraps(Wraps<Filter> logicWraps) {
        this.logicWraps = logicWraps;
    }

    private Delete add(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLast(new WrapsObject<Filter>(filter), logic);
        return this;
    }

    @Override
    public Delete setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public Delete linked(WrapsLinked linked) {
        Wraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Delete and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    @Override
    public Delete or() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.OR);
        }
        return this;
    }

    @Override
    public Query covert2query() {
        DefaultQuery query = new DefaultQuery(logicWraps, this.tableClass);
        return query;
    }

    @Override
    public Delete eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }
}
