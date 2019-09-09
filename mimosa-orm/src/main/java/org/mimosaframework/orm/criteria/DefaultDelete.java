package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BeanSession;
import org.mimosaframework.orm.Session;

import java.util.List;

/**
 * @author yangankang
 */
public class DefaultDelete implements Delete {
    private LogicWraps<Filter> logicWraps;
    private Class tableClass;
    private Session session;
    private BeanSession beanSession;

    public DefaultDelete(Class tableClass) {
        this.tableClass = tableClass;
    }

    public DefaultDelete(Class tableClass, Session session) {
        this.tableClass = tableClass;
        this.session = session;
    }

    public DefaultDelete(Class tableClass, BeanSession beanSession) {
        this.tableClass = tableClass;
        this.beanSession = beanSession;
    }

    public Class getTableClass() {
        return tableClass;
    }

    public LogicWraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public void setLogicWraps(LogicWraps<Filter> logicWraps) {
        this.logicWraps = logicWraps;
    }

    private Delete add(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }

        this.logicWraps.addLast(new LogicWrapObject<Filter>(filter), logic);
        return this;
    }

    @Override
    public Delete setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public Delete addLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Delete andLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Delete orLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw, CriteriaLogic.OR);
        return this;
    }

    @Override
    public Delete and(Filter filter) {
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Delete or(Filter filter) {
        this.add(filter, CriteriaLogic.OR);
        return this;
    }

    @Override
    public Filter addFilter() {
        Filter filter = new DefaultFilter(this);
        this.add(filter, CriteriaLogic.AND);
        return filter;
    }

    @Override
    public void delete() {
        if (this.session != null) {
            this.session.delete(this);
        }

        if (this.beanSession != null) {
            this.beanSession.delete(this);
        }
    }

    @Override
    public Query query() {
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
