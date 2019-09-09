package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BeanSession;
import org.mimosaframework.orm.Session;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yangankang
 */
public class DefaultUpdate implements Update {
    private LogicWraps<Filter> logicWraps;
    private Map<Object, Object> values = new LinkedHashMap<Object, Object>();
    private Class tableClass;
    private Session session;
    private BeanSession beanSession;

    public DefaultUpdate(Class tableClass) {
        this.tableClass = tableClass;
    }

    public DefaultUpdate(Class tableClass, Session session) {
        this.tableClass = tableClass;
        this.session = session;
    }

    public DefaultUpdate(Class tableClass, BeanSession beanSession) {
        this.tableClass = tableClass;
        this.beanSession = beanSession;
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

    public LogicWraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public void setLogicWraps(LogicWraps<Filter> logicWraps) {
        this.logicWraps = logicWraps;
    }

    private Update add(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLast(new LogicWrapObject<Filter>(filter), logic);
        return this;
    }

    @Override
    public Update add(Object key, Object value) {
        values.put(key, value);
        return this;
    }

    @Override
    public Update value(Object key, Object value) {
        values.put(key, value);
        return this;
    }

    @Override
    public Update addSelf(Object key) {
        UpdateSetValue v = new UpdateSetValue();
        v.setObject(UpdateSpecialType.ADD_SELF);
        values.put(key, v);
        return this;
    }

    @Override
    public Update subSelf(Object key) {
        UpdateSetValue v = new UpdateSetValue();
        v.setObject(UpdateSpecialType.SUB_SELF);
        values.put(key, v);
        return this;
    }

    @Override
    public Update addSelf(Object key, Integer step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setObject(UpdateSpecialType.ADD_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public Update subSelf(Object key, Integer step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setObject(UpdateSpecialType.SUB_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public Update addSelf(Object key, String step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setObject(UpdateSpecialType.ADD_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public Update subSelf(Object key, String step) {
        UpdateSetValue v = new UpdateSetValue();
        v.setObject(UpdateSpecialType.SUB_SELF);
        v.setStep(step);
        values.put(key, v);
        return this;
    }

    @Override
    public Update setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public Update addLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Update andLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Update orLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw, CriteriaLogic.OR);
        return this;
    }

    @Override
    public Update and(Filter filter) {
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update or(Filter filter) {
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
    public void update() {
        if (this.session != null) {
            this.session.update(this);
        }

        if (this.beanSession != null) {
            this.beanSession.update(this);
        }
    }

    @Override
    public Query query() {
        DefaultQuery query = new DefaultQuery(logicWraps, this.tableClass);
        return query;
    }

    @Override
    public Update eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Update isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }
}
