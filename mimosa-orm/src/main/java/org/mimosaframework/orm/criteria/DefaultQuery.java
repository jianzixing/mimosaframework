package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.BeanSessionTemplate;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.utils.SQLUtils;

import java.util.*;

/**
 * @author yangankang
 */
public class DefaultQuery<T> extends AbstractFilter<LogicQuery<T>> implements LogicQuery<T> {
    private SessionTemplate sessionTemplate;
    private BeanSessionTemplate beanSessionTemplate;

    private Wraps<Filter> logicWraps;

    /**
     * 包含所有的join查询，每个join查询拥有独立的父子结构，
     * 用于查询时数据包装。
     */
    private Set<Join> joins = new LinkedHashSet<>();
    private Set<OrderBy> orderBy = new LinkedHashSet<>();
    private Map<Class, List<String>> fields = new HashMap<>();
    private Map<Class, List<String>> excludes = new HashMap<>();

    private Limit limit;
    private Class<?> tableClass;
    private boolean isForUpdate = false;
    private boolean isMaster = true;
    private String slaveName;
    private String as;

    /**
     * 查询数据方式
     */
    private QueryType type;
    private boolean withoutOrderBy = false;

    public DefaultQuery(Class<?> tableClass) {
        this.tableClass = tableClass;
    }

    public DefaultQuery(SessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public DefaultQuery(SessionTemplate sessionTemplate, Class<?> tableClass) {
        this.sessionTemplate = sessionTemplate;
        this.tableClass = tableClass;
    }

    public DefaultQuery(BeanSessionTemplate beanSessionTemplate) {
        this.beanSessionTemplate = beanSessionTemplate;
    }

    public DefaultQuery(BeanSessionTemplate beanSessionTemplate, Class<?> tableClass) {
        this.beanSessionTemplate = beanSessionTemplate;
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
        query.orderBy = orderBy;
        query.fields = fields;
        query.limit = limit;
        query.tableClass = tableClass;
        query.isMaster = isMaster;
        query.slaveName = slaveName;
        query.as = as;
        return query;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    @Override
    public Paging paging() {
        if (this.sessionTemplate != null) {
            return this.sessionTemplate.paging(this);
        }
        if (this.beanSessionTemplate != null) {
            return this.beanSessionTemplate.paging(this);
        }

        return null;
    }

    @Override
    public T get() {
        if (this.sessionTemplate != null) {
            return (T) this.sessionTemplate.get(this);
        }
        if (this.beanSessionTemplate != null) {
            return this.beanSessionTemplate.get(this);
        }
        return null;
    }

    @Override
    public List<T> list() {
        if (this.sessionTemplate != null) {
            return (List<T>) this.sessionTemplate.list(this);
        }
        if (this.beanSessionTemplate != null) {
            return this.beanSessionTemplate.list(this);
        }
        return null;
    }

    @Override
    public long count() {
        if (this.sessionTemplate != null) {
            return this.sessionTemplate.count(this);
        }
        if (this.beanSessionTemplate != null) {
            return this.beanSessionTemplate.count(this);
        }
        return 0;
    }

    @Override
    public LogicQuery as(String as) {
        SQLUtils.checkAsName(as);
        this.as = as;
        return this;
    }

    public Limit getLimit() {
        return limit;
    }

    @Override
    public LogicQuery filter(DefaultFilter as) {
        this.addFilterInNested(as);
        return this;
    }

    @Override
    public LogicQuery nested(WrapsNested nested) {
        if (nested != null) {
            if (this.logicWraps == null) {
                this.logicWraps = new Wraps<>();
            }
            this.logicWraps.addLastLink(nested.getLogicWraps());
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
    public LogicQuery<T> orderBy(OrderBy order) {
        if (withoutOrderBy == false) this.orderBy.add(order);
        return this;
    }

    @Override
    public LogicQuery<T> limit(Limit limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public LogicQuery<T> setTableClass(Class<?> c) {
        this.tableClass = c;
        return this;
    }

    @Override
    public LogicQuery forUpdate() {
        this.isForUpdate = true;
        return this;
    }

    @Override
    public LogicQuery forUpdate(boolean is) {
        this.isForUpdate = is;
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
            return joins;
        }
        return null;
    }

    public void setJoins(Set<Join> joins) {
        this.joins = joins;
    }

    public Set<OrderBy> getOrderBy() {
        return orderBy;
    }

    public boolean isWithoutOrderBy() {
        return withoutOrderBy;
    }

    @Override
    public LogicQuery eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery in(Object key, Iterable values) {
        if (values == null) {
            throw new IllegalArgumentException(I18n.print("must_value"));
        }
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery in(Object key, Object... values) {
        if (key == null || values == null || values.length == 0) {
            throw new IllegalArgumentException(I18n.print("in_must_key_value"));
        }
        Filter filter = new DefaultFilter().in(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery nin(Object key, Iterable values) {
        if (values == null) {
            throw new IllegalArgumentException(I18n.print("not_in_must_value"));
        }
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery nin(Object key, Object... values) {
        if (key == null || values == null || values.length == 0) {
            throw new IllegalArgumentException(I18n.print("not_in_must_key_value"));
        }
        Filter filter = new DefaultFilter().nin(key, values);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNotNull(key);
        this.addFilterInNested(filter);
        return this;
    }

    @Override
    public LogicQuery exists(LogicQuery query) {
        Filter filter = new DefaultFilter().exists(query);
        this.addFilterInNested(filter);
        return this;
    }

    private void addFilterInNested(Filter filter) {
        if (this.logicWraps == null) {
            this.logicWraps = new Wraps<>();
            this.logicWraps.add(new WrapsObject<>(filter));
        } else {
            logicWraps.addLast(new WrapsObject<>(filter));
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
    public <F> LogicQuery fields(FieldFunction<F>... fields) {
        return this.fields(Arrays.asList(fields));
    }

    @Override
    public LogicQuery fields(Class tableClass, Object... fields) {
        return this.fields(tableClass, Arrays.asList(fields));
    }

    @Override
    public <F> LogicQuery fields(Class tableClass, FieldFunction<F>... fields) {
        return this.fields(tableClass, Arrays.asList(fields));
    }

    @Override
    public LogicQuery fields(List<Object> fields) {
        if (tableClass == null) {
            throw new IllegalArgumentException(I18n.print("not_found_table"));
        }
        return this.fields(tableClass, fields);
    }

    @Override
    public LogicQuery fields(Class tableClass, List<Object> fields) {
        if (fields != null) {
            List<String> nf = new ArrayList<>();
            for (Object field : fields) {
                if (field != null) {
                    nf.add(ClassUtils.value(field));
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
    public <F> LogicQuery excludes(FieldFunction<F>... fields) {
        return this.excludes(Arrays.asList(fields));
    }

    @Override
    public LogicQuery excludes(Class tableClass, Object... fields) {
        return this.excludes(tableClass, Arrays.asList(fields));
    }

    @Override
    public <F> LogicQuery excludes(Class tableClass, FieldFunction<F>... fields) {
        return this.excludes(tableClass, Arrays.asList(fields));
    }

    @Override
    public LogicQuery excludes(List<Object> fields) {
        if (tableClass == null) {
            throw new IllegalArgumentException(I18n.print("not_found_table"));
        }
        return this.excludes(tableClass, fields);
    }

    @Override
    public LogicQuery excludes(Class tableClass, List<Object> fields) {
        if (fields != null) {
            List<String> nf = new ArrayList<>();
            for (Object field : fields) {
                if (field != null) {
                    nf.add(ClassUtils.value(field));
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

    public boolean isForUpdate() {
        return isForUpdate;
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
    public LogicQuery<T> limit(long start, long count) {
        Limit limit = new Limit();
        limit.limit(start, count);
        this.limit(limit);
        return this;
    }

    @Override
    public LogicQuery<T> limit(long limit) {
        return this.limit(0, limit);
    }

    @Override
    public LogicQuery<T> orderBy(Object field, boolean isAsc) {
        if (!withoutOrderBy) return this.orderBy(new OrderBy(ClassUtils.value(field), isAsc));
        return null;
    }

    @Override
    public <F> LogicQuery<T> orderBy(FieldFunction<F> field, boolean isAsc) {
        return this.orderBy((Object) field, isAsc);
    }

    @Override
    public LogicQuery<T> orderBy(Object field, Sort sort) {
        return this.orderBy(field, sort.isAsc());
    }

    @Override
    public <F> LogicQuery<T> orderBy(FieldFunction<F> field, Sort sort) {
        return this.orderBy((Object) field, sort.isAsc());
    }

    @Override
    public LogicQuery<T> orderByAsc(Object field) {
        return this.orderBy(field, true);
    }

    @Override
    public <F> LogicQuery<T> orderByAsc(FieldFunction<F> field) {
        return this.orderBy(field, true);
    }

    @Override
    public LogicQuery<T> orderByDesc(Object field) {
        return this.orderBy(field, false);
    }

    @Override
    public <F> LogicQuery<T> orderByDesc(FieldFunction<F> field) {
        return this.orderBy(field, false);
    }

    @Override
    public LogicQuery<T> withoutOrderBy() {
        this.orderBy.clear();
        this.withoutOrderBy = true;
        return this;
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

    public String getAs() {
        return as;
    }

    public String getQueryTableAs() {
        if (StringTools.isNotEmpty(as)) return as;
        return "T";
    }

    public void setAs(String as) {
        this.as = as;
    }

    private void checkJoinHasOnFilter(Set<Join> joins) {
        if (joins != null && joins.size() > 0) {
            List<String> list = new ArrayList<>(joins.size());
            for (Join join : joins) {
                DefaultJoin dj = (DefaultJoin) join;
                if (dj.getOns() == null || dj.getOns().size() == 0) {
                    throw new IllegalArgumentException(I18n.print("join_not_have_filter", dj.getTable().getSimpleName()));
                }
                if (StringTools.isNotEmpty(dj.getAs())) {
                    if (list.contains(dj.getAs())) {
                        throw new IllegalArgumentException(I18n.print("join_as_repeat", dj.getAs()));
                    }
                    list.add(dj.getAs());
                }
            }
        }
    }

    public void checkQuery() {
        this.checkJoinHasOnFilter(joins);
    }

    public boolean hasFilter(String name) {
        if (this.logicWraps != null) {
            Iterator<WrapsObject<Filter>> iterator = this.logicWraps.iterator();
            while (iterator.hasNext()) {
                WrapsObject<Filter> next = iterator.next();
                DefaultFilter filter = (DefaultFilter) next.getWhere();
                if (name.equalsIgnoreCase(String.valueOf(filter.getKey()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
