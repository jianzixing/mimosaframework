package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

import java.util.ArrayList;
import java.util.List;

public class DefaultFunction implements LogicFunction {
    private List<FunctionField> funs = null;
    private Class tableClass;
    private boolean isMaster = true;
    private String slaveName;
    private Wraps<Filter> logicWraps;
    private List groupBy = null;
    private List<Order> orderBy = null;
    private List childGroupBy = null;

    public DefaultFunction() {
    }

    public DefaultFunction(Class tableClass) {
        this.tableClass = tableClass;
    }

    @Override
    public Function addFunction(BasicFunction function, Object field) {
        if (funs == null) {
            funs = new ArrayList<>();
        }
        funs.add(new FunctionField(field, function));
        return this;
    }

    @Override
    public Function addFunction(BasicFunction function, Object field, String alias) {
        if (funs == null) {
            funs = new ArrayList<>();
        }
        funs.add(new FunctionField(field, function, alias));
        return this;
    }

    @Override
    public Function addFunction(FunctionField function) {
        if (funs == null) {
            funs = new ArrayList<>();
        }
        funs.add(function);
        return this;
    }

    @Override
    public Function master() {
        isMaster = true;
        return this;
    }

    @Override
    public Function slave() {
        isMaster = false;
        return this;
    }

    @Override
    public Function slave(String name) {
        isMaster = false;
        slaveName = name;
        return this;
    }

    private Function add(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
        }

        this.logicWraps.addLast(new WrapsObject<Filter>(filter), logic);
        return this;
    }

    @Override
    public Function linked(WrapsLinked linked) {
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
    public Function groupBy(Object field) {
        if (groupBy == null && field != null) {
            groupBy = new ArrayList();
        }
        if (field != null) {
            groupBy.add(field);
        }
        return this;
    }

    @Override
    public Function orderBy(Object field, boolean isAsc) {
        if (orderBy == null && field != null) {
            orderBy = new ArrayList();
        }
        if (field != null) {
            orderBy.add(new Order(isAsc, field));
        }
        return this;
    }

    @Override
    public Function childGroupBy(Object field) {
        if (childGroupBy == null && field != null) {
            childGroupBy = new ArrayList();
        }
        if (field != null) {
            childGroupBy.add(field);
        }
        return this;
    }

    @Override
    public Query covert2query() {
        DefaultQuery query = new DefaultQuery(logicWraps, this.tableClass);
        return query;
    }

    @Override
    public Function eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.add(filter, CriteriaLogic.AND);
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

    public List<FunctionField> getFuns() {
        return funs;
    }

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public List getGroupBy() {
        return groupBy;
    }

    public List<Order> getOrderBy() {
        return orderBy;
    }

    public List getChildGroupBy() {
        return childGroupBy;
    }

    public void setFuns(List<FunctionField> funs) {
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

    public void setGroupBy(List groupBy) {
        this.groupBy = groupBy;
    }

    public void setOrderBy(List<Order> orderBy) {
        this.orderBy = orderBy;
    }

    public void setChildGroupBy(List childGroupBy) {
        this.childGroupBy = childGroupBy;
    }
}
