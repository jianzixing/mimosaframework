package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BeanSessionTemplate;
import org.mimosaframework.orm.SessionTemplate;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yangankang
 */
public class DefaultUpdate implements LogicUpdate {
    private SessionTemplate sessionTemplate;
    private BeanSessionTemplate beanSessionTemplate;

    private Wraps<Filter> logicWraps;
    private Map<Object, Object> values = new LinkedHashMap<Object, Object>();
    private Class tableClass;

    public DefaultUpdate() {
    }

    public DefaultUpdate(SessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public DefaultUpdate(SessionTemplate sessionTemplate, Class tableClass) {
        this.sessionTemplate = sessionTemplate;
        this.tableClass = tableClass;
    }

    public DefaultUpdate(BeanSessionTemplate beanSessionTemplate) {
        this.beanSessionTemplate = beanSessionTemplate;
    }

    public DefaultUpdate(BeanSessionTemplate beanSessionTemplate, Class tableClass) {
        this.beanSessionTemplate = beanSessionTemplate;
        this.tableClass = tableClass;
    }

    public DefaultUpdate(Class tableClass) {
        this.tableClass = tableClass;
    }

    public Class getTableClass() {
        return tableClass;
    }

    public Map<Object, Object> getValues() {
        return values;
    }

    public void setValues(Map<Object, Object> values) {
        this.values = values;
    }

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public void setLogicWraps(Wraps<Filter> logicWraps) {
        this.logicWraps = logicWraps;
    }

    private Update add(Filter filter) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }
        this.logicWraps.addLast(new WrapsObject<Filter>(filter));
        return this;
    }

    @Override
    public Update set(Serializable key, Object value) {
        if (value == null) {
            value = Keyword.NULL;
        }
        values.put(key, value);
        return this;
    }

    @Override
    public LogicUpdate addSelf(Serializable key) {
        UpdateSetValue v = new UpdateSetValue();
        v.setType(UpdateSpecialType.ADD_SELF);
        values.put(key, v);
        return this;
    }

    @Override
    public LogicUpdate subSelf(Serializable key) {
        UpdateSetValue v = new UpdateSetValue();
        v.setType(UpdateSpecialType.SUB_SELF);
        values.put(key, v);
        return this;
    }

    @Override
    public LogicUpdate addSelf(Serializable key, long step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setType(UpdateSpecialType.ADD_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public LogicUpdate subSelf(Serializable key, long step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setType(UpdateSpecialType.SUB_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public LogicUpdate addSelf(Serializable key, String step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setType(UpdateSpecialType.ADD_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public LogicUpdate subSelf(Serializable key, String step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setType(UpdateSpecialType.SUB_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public long update() {
        if (this.sessionTemplate != null) {
            return this.sessionTemplate.update(this);
        }
        if (this.beanSessionTemplate != null) {
            return this.beanSessionTemplate.update(this);
        }
        return 0;
    }

    @Override
    public LogicUpdate setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public LogicUpdate linked(WrapsLinked linked) {
        Wraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Update and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    @Override
    public Update or() {
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
    public LogicUpdate eq(Serializable key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate in(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate in(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate nin(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate nin(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate like(Serializable key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate ne(Serializable key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate gt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate gte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate lt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate lte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate between(Serializable key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate isNull(Serializable key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.add(filter);
        return this;
    }

    @Override
    public LogicUpdate isNotNull(Serializable key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.add(filter);
        return this;
    }
}
