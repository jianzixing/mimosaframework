package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultFunction implements LogicFunction {
    private Set<FunctionField> funs = null;
    private Set<HavingField> havingFields = null;
    private Class tableClass;
    private boolean isMaster = true;
    private String slaveName;
    private Wraps<Filter> logicWraps;
    private Set groupBy = null;
    private Set<OrderBy> orderBy = null;

    public DefaultFunction() {
    }

    public DefaultFunction(Class tableClass) {
        this.tableClass = tableClass;
    }

    @Override
    public LogicFunction addFunction(BasicFunction function, Serializable field) {
        if (funs == null) {
            funs = new LinkedHashSet<>();
        }
        funs.add(new FunctionField(field, function));
        return this;
    }

    @Override
    public LogicFunction addFunction(BasicFunction function, Serializable field, String alias) {
        if (funs == null) {
            funs = new LinkedHashSet<>();
        }
        funs.add(new FunctionField(field, function, alias));
        return this;
    }

    @Override
    public LogicFunction addFunction(FunctionField function) {
        if (funs == null) {
            funs = new LinkedHashSet<>();
        }
        funs.add(function);
        return this;
    }

    @Override
    public LogicFunction master() {
        isMaster = true;
        return this;
    }

    @Override
    public LogicFunction slave() {
        isMaster = false;
        return this;
    }

    @Override
    public LogicFunction slave(String name) {
        isMaster = false;
        slaveName = name;
        return this;
    }

    private Function add(Filter filter) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLast(new WrapsObject<Filter>(filter));
        return this;
    }

    @Override
    public LogicFunction linked(WrapsLinked linked) {
        Wraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Function and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    @Override
    public Function or() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.OR);
        }
        return this;
    }

    @Override
    public LogicFunction groupBy(Object field) {
        if (groupBy == null && field != null) {
            groupBy = new LinkedHashSet();
        }
        if (field != null) {
            groupBy.add(field);
        }
        return this;
    }

    @Override
    public LogicFunction orderBy(Object field, boolean isAsc) {
        if (orderBy == null && field != null) {
            orderBy = new LinkedHashSet<>();
        }
        if (field != null) {
            orderBy.add(new OrderBy(isAsc, field));
        }
        return this;
    }

    @Override
    public LogicFunction having(HavingField field) {
        if (field != null) {
            if (this.havingFields == null) this.havingFields = new LinkedHashSet<>();
            this.havingFields.add(field);
        }
        return this;
    }

    @Override
    public Query covert2query() {
        DefaultQuery query = new DefaultQuery(logicWraps, this.tableClass);
        return query;
    }

    @Override
    public LogicFunction eq(Serializable key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction in(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction in(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction nin(Serializable key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction nin(Serializable key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction like(Serializable key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction ne(Serializable key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction gt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction gte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction lt(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction lte(Serializable key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction between(Serializable key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction isNull(Serializable key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.add(filter);
        return this;
    }

    @Override
    public LogicFunction isNotNull(Serializable key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.add(filter);
        return this;
    }

    public Class getTableClass() {
        return tableClass;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public Set<FunctionField> getFuns() {
        return funs;
    }

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public Set getGroupBy() {
        return groupBy;
    }

    public Set<OrderBy> getOrderBy() {
        return orderBy;
    }


    public void setFuns(Set<FunctionField> funs) {
        this.funs = funs;
    }

    public void setTableClass(Class tableClass) {
        this.tableClass = tableClass;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public void setLogicWraps(Wraps<Filter> logicWraps) {
        this.logicWraps = logicWraps;
    }

    public void setGroupBy(Set groupBy) {
        this.groupBy = groupBy;
    }

    public void setOrderBy(Set<OrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    public Set<HavingField> getHavingFields() {
        return havingFields;
    }

    public void setHavingFields(Set<HavingField> havingFields) {
        this.havingFields = havingFields;
    }
}
