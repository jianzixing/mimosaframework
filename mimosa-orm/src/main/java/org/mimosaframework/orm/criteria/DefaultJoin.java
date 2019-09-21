package org.mimosaframework.orm.criteria;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yangankang
 */
public class DefaultJoin implements Join {

    private List<Filter> valueFilters = new ArrayList<Filter>();
    private List<OnField> onFilters = new ArrayList<OnField>();
    private String aliasName;
    private Query query;
    private Class<?> table;
    private Class<?> mainTable;
    /**
     * 查询结果是以单独对象保存还是List方式保存
     * a:{} 和 a:[{}]
     */
    private boolean isMulti = true;
    private Join parentJoin;
    private Set<Join> childJoin;
    private String mainClassAliasName;
    private String tableClassAliasName;

    public String getMainClassAliasName() {
        return mainClassAliasName;
    }

    public void setMainClassAliasName(String mainClassAliasName) {
        this.mainClassAliasName = mainClassAliasName;
    }

    public String getTableClassAliasName() {
        return tableClassAliasName;
    }

    public void setTableClassAliasName(String tableClassAliasName) {
        this.tableClassAliasName = tableClassAliasName;
    }

    public DefaultJoin() {
    }

    public DefaultJoin(Class<?> mainTable, Class<?> selfTable) {
        this.table = selfTable;
        this.mainTable = mainTable;
    }

    public DefaultJoin(Query query, Class<?> table) {
        this.query = query;
        this.table = table;
    }

    public DefaultJoin(Query query, Class<?> mainTable, Class<?> selfTable) {
        this.query = query;
        this.table = selfTable;
        this.mainTable = mainTable;
    }

    public Class<?> getMainTable() {
        return mainTable;
    }

    public void setMainTable(Class<?> mainTable) {
        this.mainTable = mainTable;
    }

    public DefaultJoin(Class<?> table) {
        this.table = table;
    }

    public Class<?> getTable() {
        return table;
    }

    public List<Filter> getValueFilters() {
        return valueFilters;
    }

    public List<OnField> getOnFilters() {
        return onFilters;
    }

    public String getAliasName() {
        return aliasName;
    }

    public Join getParentJoin() {
        return parentJoin;
    }

    @Override
    public Query query() {
        return this.query;
    }

    @Override
    public Join parent() {
        return this.getParentJoin();
    }

    @Override
    public Join setQuery(Query query) {
        this.query = query;
        return this;
    }

    @Override
    public Join addChildJoin(Join join) {
        DefaultJoin dj = ((DefaultJoin) join);
        if (dj.getMainTable() == null) {
            if (this.table == null) {
                throw new IllegalArgumentException("JOIN的子表没有设置映射类");
            }
            dj.setMainTable(this.table);
        }
        if (dj.getMainTable() != this.table) {
            throw new IllegalArgumentException("子JOIN映射类" + dj.getMainTable().getSimpleName() +
                    "的主表和当前表" + this.table.getSimpleName() + "不一致");
        }

        this.createSetChildJoin(join);

        DefaultQuery query = ((DefaultQuery) this.query);
        if (query != null) {
            List<Join> joins = query.getLeftJoin();
            if (joins != null) {
                joins.add(join);
                return join;
            }
        }

        return this;
    }

    @Override
    public Join childJoin(Class<?> table) {
        Join join = new DefaultJoin(query, this.table, table);
        this.createSetChildJoin(join);

        DefaultQuery query = ((DefaultQuery) this.query);
        if (query != null) {
            List<Join> joins = query.getLeftJoin();
            if (joins != null) {
                joins.add(join);
                return join;
            }
        }
        return join;
    }

    @Override
    public Join add(Filter filter) {
        if (filter instanceof DefaultFilter) {
            this.valueFilters.add(filter);
        } else {
            throw new IllegalArgumentException("仅支持DefaultFilter类型");
        }
        return this;
    }

    @Override
    public Filter filter() {
        Filter filter = new DefaultFilter(this);
        this.valueFilters.add(filter);
        return filter;
    }

    private void checkFieldClass(Object self, Object mainField) {
        if (self.getClass().equals(mainTable)
                && mainField.getClass().equals(table) && mainTable != table) {
            throw new IllegalArgumentException("当前字段对应关系相反");
        }
    }

    @Override
    public Join join(Object self, Object value) {
        if (value instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) value).getValue(), "="));
        } else {
            this.onFilters.add(new OnField(self, value, "="));
        }
        this.checkFieldClass(self, value);
        return this;
    }

    @Override
    public Join eq(Object self, Object mainField) {
        if (mainField instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) mainField).getValue(), "="));
        } else {
            this.onFilters.add(new OnField(self, mainField, "="));
        }
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public Join ne(Object self, Object mainField) {
        if (mainField instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) mainField).getValue(), "!="));
        } else {
            this.onFilters.add(new OnField(self, mainField, "!="));
        }
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public Join gt(Object self, Object mainField) {
        if (mainField instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) mainField).getValue(), ">"));
        } else {
            this.onFilters.add(new OnField(self, mainField, ">"));
        }
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public Join gte(Object self, Object queryField) {
        if (queryField instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) queryField).getValue(), ">="));
        } else {
            this.onFilters.add(new OnField(self, queryField, ">="));
        }
        this.checkFieldClass(self, queryField);
        return this;
    }

    @Override
    public Join lt(Object self, Object mainField) {
        if (mainField instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) mainField).getValue(), "<"));
        } else {
            this.onFilters.add(new OnField(self, mainField, "<"));
        }
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public Join lte(Object self, Object mainField) {
        if (mainField instanceof Value) {
            this.valueFilters.add(new DefaultFilter(self, ((Value) mainField).getValue(), "<="));
        } else {
            this.onFilters.add(new OnField(self, mainField, "<="));
        }
        this.checkFieldClass(self, mainField);
        return this;
    }

    @Override
    public Join aliasName(Object s) {
        this.aliasName = String.valueOf(s);
        return this;
    }

    @Override
    public Join single() {
        this.isMulti = false;
        return this;
    }

    public boolean isMulti() {
        return isMulti;
    }

    @Override
    public Join setMulti(boolean is) {
        this.isMulti = is;
        return this;
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
}