package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.i18n.I18n;

import java.util.*;

/**
 * @author yangankang
 */
public class DefaultQuery implements LogicQuery {

    private Wraps<Filter> logicWraps;

    /**
     * 包含所有的join查询，每个join查询拥有独立的父子结构，
     * 用于查询时数据包装。
     */
    private Set<Join> joins = new LinkedHashSet<>();
    private Set<Order> orders = new LinkedHashSet<>();
    private Map<Class, List<String>> fields = new HashMap<>();
    private Map<Class, List<String>> excludes = new HashMap<>();

    private Limit limit;
    private Class<?> tableClass;
    private boolean isMaster = true;
    private String slaveName;

    /**
     * 查询数据方式
     */
    private QueryType type;

    public DefaultQuery(Class<?> tableClass) {
        this.tableClass = tableClass;
    }

    public DefaultQuery(Wraps<Filter> logicWraps, Class<?> tableClass) {
        this.logicWraps = logicWraps;
        this.tableClass = tableClass;
    }

    public DefaultQuery() {
    }

    @Override
    public Query clone() {
        DefaultQuery query = new DefaultQuery(tableClass);
        query.logicWraps = logicWraps;
        query.joins = joins;
        query.orders = orders;
        query.fields = fields;
        query.limit = limit;
        query.tableClass = tableClass;
        query.isMaster = isMaster;
        query.slaveName = slaveName;
        return query;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public Limit getLimit() {
        return limit;
    }

    @Override
    public LogicQuery linked(WrapsLinked linked) {
        if (linked != null) {
            if (this.logicWraps == null) {
                this.logicWraps = new Wraps<>();
            }
            this.logicWraps.addLastLink(linked.getLogicWraps());
        }
        return this;
    }

    @Override
    public Query and() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.AND);
        }
        return this;
    }

    @Override
    public Query or() {
        if (this.logicWraps != null && this.logicWraps.size() > 0) {
            this.logicWraps.getLast().setLogic(CriteriaLogic.OR);
        }
        return this;
    }

    @Override
    public LogicQuery subjoin(Join join) {
        DefaultJoin dj = ((DefaultJoin) join);
        if (dj.getMainTable() == null) {
            if (this.tableClass == null) {
                throw new IllegalArgumentException(I18n.print("not_found_table"));
            }
            dj.setMainTable(this.tableClass);
        }
        if (dj.getMainTable() != this.tableClass) {
            throw new IllegalArgumentException(I18n.print("sub_table_diff",
                    dj.getMainTable().getSimpleName(), this.tableClass.getSimpleName()));
        }
        this.joins.add(join);

        this.setLeftChildTop(dj);
        return this;
    }

    private void setLeftChildTop(DefaultJoin dj) {
        if (this.joins != null && !this.joins.contains(dj)) {
            this.joins.add(dj);
        }

        Set<Join> joins = dj.getChildJoin();
        if (joins != null) {
            for (Join join : joins) {
                this.setLeftChildTop((DefaultJoin) join);
            }
        }
    }

    @Override
    public LogicQuery order(Order order) {
        this.orders.add(order);
        return this;
    }

    @Override
    public LogicQuery limit(Limit limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public LogicQuery setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    public Wraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public Set<Join> getJoins() {
        return joins;
    }

    public Set<Join> getTopJoin() {
        if (this.joins != null && this.joins.size() > 0) {
            Set<Join> joins = new LinkedHashSet<>();
            for (Join join : this.joins) {
                DefaultJoin j = (DefaultJoin) join;
                if (j.getParentJoin() == null) {
                    joins.add(j);
                }
            }
        }
        return null;
    }

    public void setJoins(Set<Join> joins) {
        this.joins = joins;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    @Override
    public LogicQuery eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery in(Object key, Iterable values) {
        if (values == null) {
            throw new IllegalArgumentException(I18n.print("must_value"));
        }
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery in(Object key, Object... values) {
        if (key == null || values == null || values.length == 0) {
            throw new IllegalArgumentException(I18n.print("in_must_key_value"));
        }
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery nin(Object key, Iterable values) {
        if (values == null) {
            throw new IllegalArgumentException(I18n.print("not_in_must_value"));
        }
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery nin(Object key, Object... values) {
        if (key == null || values == null || values.length == 0) {
            throw new IllegalArgumentException(I18n.print("not_in_must_key_value"));
        }
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public LogicQuery isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    private void addFilterInLinked(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
            this.logicWraps.add(new WrapsObject<Filter>(filter));
        } else {
            logicWraps.addLast(new WrapsObject<Filter>(filter), logic);
        }
    }

    @Override
    public LogicQuery master() {
        this.isMaster = true;
        return this;
    }

    @Override
    public LogicQuery slave() {
        this.isMaster = false;
        return this;
    }

    @Override
    public LogicQuery slave(String name) {
        this.isMaster = false;
        this.slaveName = name;
        return this;
    }

    @Override
    public LogicQuery fields(Object... fields) {
        return this.fields(Arrays.asList(fields));
    }

    @Override
    public LogicQuery fields(Class tableClass, Object... fields) {
        return this.fields(tableClass, Arrays.asList(fields));
    }

    @Override
    public LogicQuery fields(List fields) {
        if (tableClass == null) {
            throw new IllegalArgumentException(I18n.print("not_found_table"));
        }
        return this.fields(tableClass, fields);
    }

    @Override
    public LogicQuery fields(Class tableClass, List fields) {
        if (fields != null) {
            List<String> nf = new ArrayList<>();
            for (Object field : fields) {
                if (field != null) {
                    nf.add(String.valueOf(field));
                }
            }
            this.fields.put(tableClass, nf);
        }
        return this;
    }

    @Override
    public LogicQuery excludes(Object... fields) {
        return this.excludes(Arrays.asList(fields));
    }

    @Override
    public LogicQuery excludes(Class tableClass, Object... fields) {
        return this.excludes(tableClass, Arrays.asList(fields));
    }

    @Override
    public LogicQuery excludes(List fields) {
        if (tableClass == null) {
            throw new IllegalArgumentException(I18n.print("not_found_table"));
        }
        return this.excludes(tableClass, fields);
    }

    @Override
    public LogicQuery excludes(Class tableClass, List fields) {
        if (fields != null) {
            List<String> nf = new ArrayList<>();
            for (Object field : fields) {
                if (field != null) {
                    nf.add(String.valueOf(field));
                }
            }
            this.excludes.put(tableClass, nf);
        }
        return this;
    }

    public Map<Class, List<String>> getFields() {
        return fields;
    }

    public Map<Class, List<String>> getExcludes() {
        return excludes;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public void setLogicWraps(Wraps<Filter> logicWraps) {
        this.logicWraps = logicWraps;
    }

    public void setFields(Map<Class, List<String>> fields) {
        this.fields = fields;
    }

    public void setExcludes(Map<Class, List<String>> excludes) {
        this.excludes = excludes;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public QueryType getType() {
        return type;
    }

    public void setType(QueryType type) {
        this.type = type;
    }

    @Override
    public LogicQuery limit(long start, long count) {
        Limit limit = new Limit();
        limit.limit(start, count);
        this.limit(limit);
        return this;
    }

    @Override
    public LogicQuery order(Object field, boolean isAsc) {
        return this.order(new Order(isAsc, field));
    }

    public void clearLeftJoin() {
        joins = new LinkedHashSet<>(1);
    }

    public void removeLimit() {
        limit = null;
    }

    public void clearFilters() {
        this.logicWraps = null;
    }

    private void checkJoinHasOnFilter(Set<Join> joins) {
        if (joins != null
                && joins.size() > 0) {
            for (Join join : joins) {
                DefaultJoin dj = (DefaultJoin) join;
                if (dj.getOns() == null || dj.getOns().size() == 0) {
                    throw new IllegalArgumentException(I18n.print("join_not_have_filter", dj.getTable().getSimpleName()));
                }
            }
        }
    }

    public void checkQuery() {
        this.checkJoinHasOnFilter(joins);
    }
}
