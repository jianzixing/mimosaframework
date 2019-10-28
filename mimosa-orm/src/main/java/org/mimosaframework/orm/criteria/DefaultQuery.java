package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.BeanSession;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import java.util.*;

/**
 * @author yangankang
 */
public class DefaultQuery<T> implements Query<T> {

    private LogicWraps<Filter> logicWraps;

    /**
     * 包含所有的join查询，每个join查询拥有独立的父子结构，
     * 用于查询时数据包装。
     */
    private List<Join> leftJoin = new LinkedList<Join>();
    private List<Join> innerJoin = new LinkedList<Join>();
    private List<Order> orders = new LinkedList<Order>();
    private Map<Class, List<String>> fields = new HashMap<>();
    private Map<Class, List<String>> excludes = new HashMap<>();

    private Limit limit;
    private Class<?> tableClass;
    private boolean isMaster = true;
    private String slaveName;
    private Session session;
    private BeanSession beanSession;

    public DefaultQuery(LogicWraps<Filter> logicWraps, Class<?> tableClass) {
        this.logicWraps = logicWraps;
        this.tableClass = tableClass;
    }

    public DefaultQuery() {
    }

    @Override
    public Query<T> clone() {
        DefaultQuery query = new DefaultQuery(tableClass);
        query.logicWraps = logicWraps;
        query.leftJoin = leftJoin;
        query.orders = orders;
        query.fields = fields;
        query.limit = limit;
        query.tableClass = tableClass;
        query.session = session;
        query.isMaster = isMaster;
        query.slaveName = slaveName;
        return query;
    }

    public DefaultFilter hasFilterByField(String field) {
        if (logicWraps != null) {
            boolean isAllAnd = true;
            DefaultFilter r = null;
            for (LogicWrapObject lwo : logicWraps) {
                DefaultFilter f = (DefaultFilter) lwo.getWhere();
                if (f == null) {
                    isAllAnd = false;
                } else {
                    if (lwo.getLogic() != CriteriaLogic.AND) {
                        isAllAnd = false;
                    }
                    if (String.valueOf(f.getKey()).equals(field) && f.getSymbol().equals("=")) {
                        r = f;
                    }
                }
            }
            if (isAllAnd && r != null) {
                return r;
            }
        }
        return null;
    }

    public DefaultFilter getFilterByField(String field) {
        if (logicWraps != null) {
            for (LogicWrapObject lwo : logicWraps) {
                DefaultFilter f = (DefaultFilter) lwo.getWhere();
                if (f.getKey().equals(field)) {
                    return f;
                }
            }
        }
        return null;
    }

    public DefaultQuery(Class<?> table) {
        this.tableClass = table;
    }

    public DefaultQuery(Class<?> table, Session session) {
        this.tableClass = table;
        this.session = session;
    }

    public DefaultQuery(Class<?> table, BeanSession beanSession) {
        this.tableClass = table;
        this.beanSession = beanSession;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public Limit getLimit() {
        return limit;
    }

    @Override
    public Query addLinked(LogicLinked linked) {
        if (linked != null) {
            if (this.logicWraps == null) {
                this.logicWraps = new LogicWraps<>();
            }
            this.logicWraps.addLastLink(linked.getLogicWraps());
        }
        return this;
    }

    @Override
    public Query andLinked(LogicLinked linked) {
        if (linked != null) {
            if (this.logicWraps == null) {
                this.logicWraps = new LogicWraps<>();
            }
            this.logicWraps.addLastLink(linked.getLogicWraps());
        }
        return this;
    }

    @Override
    public Query orLinked(LogicLinked linked) {
        if (linked != null) {
            if (this.logicWraps == null) {
                this.logicWraps = new LogicWraps<>();
            }
            this.logicWraps.addLastLink(linked.getLogicWraps(), CriteriaLogic.OR);
        }
        return this;
    }

    @Override
    public Query and(Filter filter) {
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query or(Filter filter) {
        this.addFilterInLinked(filter, CriteriaLogic.OR);
        return this;
    }

    @Override
    public Filter addFilter() {
        Filter filter = new DefaultFilter(this);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return filter;
    }

    @Override
    public Query addSubjoin(Join join) {
        DefaultJoin dj = ((DefaultJoin) join);
        if (dj.getMainTable() == null) {
            if (this.tableClass == null) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_found_table"));
            }
            dj.setMainTable(this.tableClass);
        }
        if (dj.getMainTable() != this.tableClass) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "sub_table_diff",
                    dj.getMainTable().getSimpleName(), this.tableClass.getSimpleName()));
        }
        this.leftJoin.add(join);

        this.setLeftChildTop(dj);
        return this;
    }

    private void setLeftChildTop(DefaultJoin dj) {
        if (this.leftJoin != null && !this.leftJoin.contains(dj)) {
            this.leftJoin.add(dj);
        }

        dj.setQuery(this);

        Set<Join> joins = dj.getChildJoin();
        if (joins != null) {
            for (Join join : joins) {
                this.setLeftChildTop((DefaultJoin) join);
            }
        }
    }

    @Override
    public Join subjoin(Class<?> table) {
        if (this.tableClass == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_found_table"));
        }
        Join join = new DefaultJoin(this, this.tableClass, table);
        this.leftJoin.add(join);
        return join;
    }

    @Override
    public Query addOrder(Order order) {
        order.setOrderTableClass(tableClass);
        this.orders.add(order);
        return this;
    }

    @Override
    public Order order() {
        Order order = new Order();
        order.setOrderTableClass(tableClass);
        this.orders.add(order);
        return order;
    }

    @Override
    public Query addLimit(Limit limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public LimitInterface limit() {
        this.limit = new Limit(this);
        return this.limit;
    }

    @Override
    public List<T> list() {
        if (this.tableClass.isEnum()) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_allow_java_bean"));
        }
        if (this.session != null) {
            return this.beanSession.list(this);
        }
        return null;
    }

    @Override
    public List<ModelObject> queries() {
        if (this.session != null) {
            return this.session.list(this);
        }
        return null;
    }

    @Override
    public ModelObject query() {
        if (this.session != null) {
            return this.session.get(this);
        }
        return null;
    }

    @Override
    public boolean hasWhere() {
        return logicWraps == null ? false : !logicWraps.isEmpty();
    }

    @Override
    public long count() {
        return this.session.count(this);
    }

    @Override
    public Paging paging() {
        return this.session.paging(this);
    }

    @Override
    public T get() {
        if (this.tableClass.isEnum()) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_allow_java_bean"));
        }
        if (this.session != null) {
            return this.beanSession.get(this);
        }
        return null;
    }

    @Override
    public Query setTableClass(Class c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public Query from(ModelObject object) {
        if (object == null) {
            return this;
        }
        object.clearEmpty();

        Set set = object.keySet();
        for (Object key : set) {
            String k = String.valueOf(key);
            if (k.startsWith("eq_")) {
                this.eq(k.replaceFirst("eq_", ""), object.get(key));
            } else if (k.startsWith("in_")) {
                ModelArray array = object.getModelArray(key);
                if (array != null) {
                    this.in(k.replaceFirst("in_", ""), array);
                }
            } else if (k.startsWith("like_")) {
                this.like(k.replaceFirst("like_", ""), "%" + object.get(key) + "%");
            } else if (k.startsWith("neq_")) {
                this.ne(k.replaceFirst("neq_", ""), object.get(key));
            } else if (k.startsWith("gt_")) {
                this.gt(k.replaceFirst("gt_", ""), object.get(key));
            } else if (k.startsWith("gteq_")) {
                this.gte(k.replaceFirst("gteq_", ""), object.get(key));
            } else if (k.startsWith("lt_")) {
                this.lt(k.replaceFirst("lt_", ""), object.get(key));
            } else if (k.startsWith("lteq_")) {
                this.lte(k.replaceFirst("lteq_", ""), object.get(key));
            } else if (k.startsWith("btn_")) {
                ModelObject b = object.getModelObject(key);
                if (b != null) {
                    this.between(k.replaceFirst("btn_", ""), b.get("start"), b.get("end"));
                }
            } else {
                Object o = object.get(key);
                if (o instanceof List) {
                    this.in(k, (List<?>) o);
                } else {
                    this.eq(k, o);
                }
            }
        }

        return this;
    }

    @Override
    public Query fromBean(Object o) {
        return this.from((ModelObject) ModelObject.toJSON(o));
    }

    public LogicWraps<Filter> getLogicWraps() {
        return logicWraps;
    }

    public List<Join> getLeftJoin() {
        return leftJoin;
    }

    public void setLeftJoin(List<Join> leftJoin) {
        this.leftJoin = leftJoin;
    }

    public List<Join> getInnerJoin() {
        return innerJoin;
    }

    public void setInnerJoin(List<Join> innerJoin) {
        this.innerJoin = innerJoin;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        if (orders != null) {
            for (Order order : orders) {
                order.setOrderTableClass(tableClass);
            }
        }
        this.orders = orders;
    }

    @Override
    public Query eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query in(Object key, Iterable values) {
        if (values == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "must_value"));
        }
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query in(Object key, Object... values) {
        if (key == null || values == null || values.length == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "in_must_key_value"));
        }
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query nin(Object key, Iterable values) {
        if (values == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_in_must_value"));
        }
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query nin(Object key, Object... values) {
        if (key == null || values == null || values.length == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_in_must_key_value"));
        }
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Query isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInLinked(filter, CriteriaLogic.AND);
        return this;
    }

    private void addFilterInLinked(Filter filter, CriteriaLogic logic) {
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
            this.logicWraps.add(new LogicWrapObject<Filter>(filter));
        } else {
            logicWraps.addLast(new LogicWrapObject<Filter>(filter), logic);
        }
    }

    @Override
    public Query master() {
        this.isMaster = true;
        return this;
    }

    @Override
    public Query slave() {
        this.isMaster = false;
        return this;
    }

    @Override
    public Query slave(String name) {
        this.isMaster = false;
        this.slaveName = name;
        return this;
    }

    @Override
    public Query fields(Object... fields) {
        return this.fields(Arrays.asList(fields));
    }

    @Override
    public Query fields(Class tableClass, Object... fields) {
        return this.fields(tableClass, Arrays.asList(fields));
    }

    @Override
    public Query fields(List fields) {
        if (tableClass == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_found_table"));
        }
        return this.fields(tableClass, fields);
    }

    @Override
    public Query fields(Class tableClass, List fields) {
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
    public Query excludes(Object... fields) {
        return this.excludes(Arrays.asList(fields));
    }

    @Override
    public Query excludes(Class tableClass, Object... fields) {
        return this.excludes(tableClass, Arrays.asList(fields));
    }

    @Override
    public Query excludes(List fields) {
        if (tableClass == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultQuery.class, "not_found_table"));
        }
        return this.excludes(tableClass, fields);
    }

    @Override
    public Query excludes(Class tableClass, List fields) {
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

    @Override
    public List<Join> getSubjoins() {
        return new ArrayList<>(this.leftJoin);
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

    public void setLogicWraps(LogicWraps<Filter> logicWraps) {
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

    @Override
    public Query limit(long start, long count) {
        Limit limit = new Limit(this);
        limit.limit(start, count);
        this.addLimit(limit);
        return this;
    }

    @Override
    public Query order(Object field, boolean isAsc) {
        return this.addOrder(new Order(isAsc, field));
    }

    @Override
    public Query order(Class tableClass, Object field, boolean isAsc) {
        return this.addOrder(new Order(tableClass, field, isAsc));
    }

    public void clearLeftJoin() {
        leftJoin = new ArrayList<Join>(1);
    }

    public void removeLimit() {
        limit = null;
    }

    public void clearFilters() {
        this.logicWraps = null;
    }

    public void clearInnerJoin() {
        if (this.innerJoin != null) {
            this.innerJoin = new ArrayList<>(1);
        }
    }

    private void checkJoinHasOnFilter(List<Join> joins) {
        if (joins != null
                && joins.size() > 0) {
            for (Join join : joins) {
                DefaultJoin dj = (DefaultJoin) join;
                if (dj.getOnFilters() == null || dj.getOnFilters().size() == 0) {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            DefaultQuery.class, "join_not_have_filter", dj.getTable().getSimpleName()));
                }
            }
        }
    }

    public void checkQuery() {
        this.checkJoinHasOnFilter(leftJoin);
    }

    public boolean checkHasFilter() {
        for (LogicWrapObject<Filter> lw : logicWraps) {
            if (lw.getWhere() != null && ((DefaultFilter) lw.getWhere()).getKey() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInnerJoin() {
        return !(innerJoin == null || innerJoin.size() == 0);
    }

    public boolean hasLeftJoin() {
        return !(leftJoin == null || leftJoin.size() == 0);
    }
}
