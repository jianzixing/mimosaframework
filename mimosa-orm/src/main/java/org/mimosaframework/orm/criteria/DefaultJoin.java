package org.mimosaframework.orm.criteria;


import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.utils.SQLUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yangankang
 */
public class DefaultJoin extends AbstractFilter<Join> implements Join {
    private List<JoinOnFilter> ons = new ArrayList<JoinOnFilter>();
    private String aliasName;
    private Class<?> table;
    private Class<?> mainTable;
    private String as;
    /**
     * 查询结果是以单独对象保存还是List方式保存
     * a:{} 和 a:[{}]
     */
    private boolean isMulti = true;
    /**
     * 是否忽略，如果忽略则会将其所有的子join的值全部移动到父join中
     */
    private boolean ignore = false;
    private Join parentJoin;
    private Set<Join> childJoin;

    /**
     * join 类型 0 left join   1 inner join
     */
    private int joinType;
    private Set<OrderBy> orderBy;

    public DefaultJoin() {
    }

    public DefaultJoin(Class<?> table) {
        this.table = table;
    }

    public DefaultJoin(Class<?> mainTable, Class<?> selfTable) {
        this.table = selfTable;
        this.mainTable = mainTable;
    }

    public DefaultJoin(Class<?> table, int joinType) {
        this.table = table;
        this.joinType = joinType;
    }

    public DefaultJoin(Class<?> mainTable, Class<?> table, int joinType) {
        this.table = table;
        this.mainTable = mainTable;
        this.joinType = joinType;
    }

    public List<JoinOnFilter> getOns() {
        return ons;
    }

    public String getAliasName() {
        return aliasName;
    }

    public Class<?> getTable() {
        return table;
    }

    public Class<?> getMainTable() {
        return mainTable;
    }

    public Join getParentJoin() {
        return parentJoin;
    }

    public void setTable(Class<?> table) {
        this.table = table;
    }

    public void setMainTable(Class<?> mainTable) {
        this.mainTable = mainTable;
    }

    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    public String getAs() {
        return as;
    }

    public boolean isIgnore() {
        return ignore;
    }

    @Override
    public Join subjoin(Join join) {
        DefaultJoin dj = ((DefaultJoin) join);
        if (dj.getMainTable() == null) {
            if (this.table == null) {
                throw new IllegalArgumentException(I18n.print("join_must_table"));
            }
            dj.setMainTable(this.table);
        }
        if (dj.getMainTable() != this.table) {
            throw new IllegalArgumentException(I18n.print("join_table_diff",
                    dj.getMainTable().getSimpleName(), this.table.getSimpleName()));
        }

        this.createSetChildJoin(join);
        return this;
    }

    private void checkFieldClass(Object self, Object mainField) {
        if (self.getClass().equals(mainTable)
            && mainField.getClass().equals(table) && mainTable != table) {
            throw new IllegalArgumentException(I18n.print("rel_reversal"));
        }
    }

    @Override
    public Join on(Object self, Object mainField) {
        return this.oneq(self, mainField);
    }

    @Override
    public <L, R> Join on(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.on((Object) self, (Object) mainField);
    }

    @Override
    public Join oneq(Object self, Object mainField) {
        this.ons.add(new JoinOnFilter(new OnField(self, mainField, "=")));
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public <L, R> Join oneq(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.oneq((Object) self, (Object) mainField);
    }

    @Override
    public Join onne(Object self, Object mainField) {
        this.ons.add(new JoinOnFilter(new OnField(self, mainField, "!=")));
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public <L, R> Join onne(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.onne((Object) self, (Object) mainField);
    }

    @Override
    public Join ongt(Object self, Object mainField) {
        this.ons.add(new JoinOnFilter(new OnField(self, mainField, ">")));
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public <L, R> Join ongt(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.ongt((Object) self, (Object) mainField);
    }

    @Override
    public Join onge(Object self, Object mainField) {
        this.ons.add(new JoinOnFilter(new OnField(self, mainField, ">=")));
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public <L, R> Join onge(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.onge((Object) self, (Object) mainField);
    }

    @Override
    public Join onlt(Object self, Object mainField) {
        this.ons.add(new JoinOnFilter(new OnField(self, mainField, "<")));
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public <L, R> Join onlt(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.onlt((Object) self, (Object) mainField);
    }

    @Override
    public Join onle(Object self, Object mainField) {
        this.ons.add(new JoinOnFilter(new OnField(self, mainField, "<=")));
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public <L, R> Join onle(FieldFunction<L> self, FieldFunction<R> mainField) {
        return this.onle((Object) self, (Object) mainField);
    }

    @Override
    public Join aliasName(Object s) {
        this.aliasName = String.valueOf(s);
        return this;
    }

    @Override
    public Join as(Object s) {
        SQLUtils.checkAsName(as);
        this.as = ClassUtils.value(s);
        return this;
    }

    @Override
    public Join single() {
        this.isMulti = false;
        return this;
    }

    @Override
    public Join multiple() {
        this.isMulti = true;
        return this;
    }

    @Override
    public Join ignore() {
        this.ignore = true;
        return this;
    }

    @Override
    public Join ignore(boolean is) {
        this.ignore = is;
        return this;
    }

    @Override
    public Join orderBy(OrderBy order) {
        if (this.orderBy == null) this.orderBy = new LinkedHashSet<>();
        this.orderBy.add(order);
        return this;
    }

    @Override
    public Join orderBy(Object field, boolean isAsc) {
        return this.orderBy(new OrderBy(ClassUtils.value(field), isAsc));
    }

    @Override
    public <F> Join orderBy(FieldFunction<F> field, boolean isAsc) {
        return this.orderBy((Object) field, isAsc);
    }

    @Override
    public Join orderBy(Object field, Sort sort) {
        return this.orderBy(field, sort.isAsc());
    }

    @Override
    public <F> Join orderBy(FieldFunction<F> field, Sort sort) {
        return this.orderBy((Object) field, sort.isAsc());
    }

    public boolean isMulti() {
        return isMulti;
    }

    @Override
    public Class getTableClass() {
        return this.table;
    }

    @Override
    public Class getMainClass() {
        return this.mainTable;
    }

    public Set<OrderBy> getOrderBy() {
        return orderBy;
    }

    public void createSetChildJoin(Join join) {
        if (childJoin == null) {
            childJoin = new LinkedHashSet<>();
        }
        childJoin.add(join);
        DefaultJoin j = (DefaultJoin) join;
        j.parentJoin = this;
    }

    public Set<Join> getChildJoin() {
        return childJoin;
    }

    public Join clone() {
        DefaultJoin join = new DefaultJoin();
        join.ons = ons;
        join.aliasName = aliasName;
        join.table = table;
        join.mainTable = mainTable;
        join.isMulti = isMulti;
        join.parentJoin = parentJoin;
        join.childJoin = childJoin;
        return join;
    }

    @Override
    public Join eq(Object key, Object value) {
        Filter filter = new DefaultFilter().eq(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join in(Object key, Iterable values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join in(Object key, Object... values) {
        Filter filter = new DefaultFilter().in(key, values);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join nin(Object key, Iterable values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join nin(Object key, Object... values) {
        Filter filter = new DefaultFilter().nin(key, values);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join like(Object key, Object value) {
        Filter filter = new DefaultFilter().like(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join ne(Object key, Object value) {
        Filter filter = new DefaultFilter().ne(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join gt(Object key, Object value) {
        Filter filter = new DefaultFilter().gt(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join gte(Object key, Object value) {
        Filter filter = new DefaultFilter().gte(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join lt(Object key, Object value) {
        Filter filter = new DefaultFilter().lt(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join lte(Object key, Object value) {
        Filter filter = new DefaultFilter().lte(key, value);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join between(Object key, Object start, Object end) {
        Filter filter = new DefaultFilter().between(key, start, end);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join isNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }

    @Override
    public Join isNotNull(Object key) {
        Filter filter = new DefaultFilter().isNull(key);
        this.ons.add(new JoinOnFilter(filter));
        return this;
    }
}
