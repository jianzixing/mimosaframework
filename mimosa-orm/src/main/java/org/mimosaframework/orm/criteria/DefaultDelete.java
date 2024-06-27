package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.BeanSessionTemplate;
import org.mimosaframework.orm.SessionTemplate;

/**
 * @author yangankang
 */
public class DefaultDelete extends AbstractFilter<LogicDelete> implements LogicDelete {
    private SessionTemplate sessionTemplate;
    private BeanSessionTemplate beanSessionTemplate;

    private Wraps<Filter> logicWraps;
    private Class tableClass;
    private boolean unsafe = false;

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

    public boolean isUnsafe() {
        return unsafe;
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
    public LogicDelete unsafe() {
        this.unsafe = true;
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
    public LogicDelete eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(ClassUtils.value(key), values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(ClassUtils.value(key), values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().nin(ClassUtils.value(key), values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().nin(ClassUtils.value(key), values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(ClassUtils.value(key), value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(ClassUtils.value(key), start, end);
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(ClassUtils.value(key));
        this.add(filter);
        return this;
    }

    @Override
    public LogicDelete isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(ClassUtils.value(key));
        this.add(filter);
        return this;
    }
}
