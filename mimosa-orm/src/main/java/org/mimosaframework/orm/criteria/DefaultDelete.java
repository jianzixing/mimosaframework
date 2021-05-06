package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BeanSessionTemplate;
import org.mimosaframework.orm.SessionTemplate;

import java.io.Serializable;

/**
 * @author yangankang
 */
public class DefaultDelete implements LogicDelete {
    private SessionTemplate sessionTemplate;
    private BeanSessionTemplate beanSessionTemplate;

    private Wraps<Filter> logicWraps;
    private Class tableClass;

    public DefaultDelete() {
    }

    public DefaultDelete(SessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public DefaultDelete(SessionTemplate sessionTemplate, Class tableClass) {
        this.sessionTemplate = sessionTemplate;
        this.tableClass = tableClass;
    }

    public DefaultDelete(BeanSessionTemplate beanSessionTemplate) {
        this.beanSessionTemplate = beanSessionTemplate;
    }

    public DefaultDelete(BeanSessionTemplate beanSessionTemplate, Class tableClass) {
        this.beanSessionTemplate = beanSessionTemplate;
        this.tableClass = tableClass;
    }

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

    private Delete add(Filter filter) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLast(new WrapsObject<Filter>(filter));
        return this;
    }

    @Override
    public LogicDelete setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public LogicDelete linked(WrapsLinked linked) {
        Wraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public long delete() {
        if (this.sessionTemplate != null) {
            return this.sessionTemplate.delete(this);
        }
        if (this.beanSessionTemplate != null) {
            return this.beanSessionTemplate.delete(this);
        }
        return 0;
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
    public LogicDelete eq(Serializable key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete in(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete in(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete nin(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete nin(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete like(Serializable key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete ne(Serializable key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete gt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete gte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete lt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete lte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete between(Serializable key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete isNull(Serializable key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete isNotNull(Serializable key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.add(filter);
        return this;
    }
}
