package org.mimosaframework.orm.criteria;

import org.mimosaframework.orm.BasicFunction;

import java.util.ArrayList;
import java.util.List;

public class DefaultFunction implements Function {
    private List<FunctionField> funs = null;
    private Class tableClass;
    private boolean isMaster = true;
    private String slaveName;
    private LogicWraps<Filter> logicWraps;
    private List groupBy = null;
    private List<Order> orderBy = null;
    private List childGroupBy = null;

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
            this.logicWraps = new LogicWraps<>();
        }

        this.logicWraps.addLast(new LogicWrapObject<Filter>(filter), logic);
        return this;
    }

    @Override
    public Function addLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }

        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Function andLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw);
        return this;
    }

    @Override
    public Function orLinked(LogicLinked linked) {
        LogicWraps lw = linked.getLogicWraps();
        if (this.logicWraps == null) {
            this.logicWraps = new LogicWraps<>();
        }
        this.logicWraps.addLastLink(lw, CriteriaLogic.OR);
        return this;
    }

    @Override
    public Function and(Filter filter) {
        this.add(filter, CriteriaLogic.AND);
        return this;
    }

    @Override
    public Function or(Filter filter) {
        this.add(filter, CriteriaLogic.OR);
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
    public Filter addFilter() {
        Filter filter = new DefaultFilter(this);
        this.add(filter, CriteriaLogic.AND);
        return filter;
    }

    @Override
    public Query query() {
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

    public LogicWraps<Filter> getLogicWraps() {
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

    public static class FunctionField {
        private Object field;
        private BasicFunction function;
        private String alias;
        // 处理精度
        private int scale;
        private String avgCountName;

        public FunctionField(Object field, BasicFunction function) {
            this.field = field;
            this.function = function;
        }

        public FunctionField(Object field, BasicFunction function, String alias) {
            this.field = field;
            this.function = function;
            this.alias = alias;
        }

        public Object getField() {
            return field;
        }

        public void setField(Object field) {
            this.field = field;
        }

        public BasicFunction getFunction() {
            return function;
        }

        public void setFunction(BasicFunction function) {
            this.function = function;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public String getAvgCountName() {
            return avgCountName;
        }

        public void setAvgCountName(String avgCountName) {
            this.avgCountName = avgCountName;
        }
    }
}
