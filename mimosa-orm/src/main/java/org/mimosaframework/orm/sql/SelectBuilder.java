package org.mimosaframework.orm.sql;

import java.util.*;

public class SelectBuilder extends Builder {
    /**
     * 如果List<Object>是null或者空则查询全部字段
     * List<Object>字段列表中如果Object属于特殊类型
     * 则特殊处理，比如函数类型 FunBuilder , FieldBuilder
     */
    private Map<Class, List<Object>> froms = new LinkedHashMap<>(1);
    private List<JoinBuilder> joinBuilders = new ArrayList<>(1);
    private WhereBuilder whereBuilder;
    private List<Object> restrict = new ArrayList<>(1);

    public SelectBuilder(Object... froms) {
        if (froms != null) {
            Class last = null;
            for (Object o : froms) {
                if (o instanceof Class) {
                    this.froms.put((Class) o, new ArrayList<Object>());
                    last = (Class) o;
                } else {
                    List<Object> fields = this.froms.get(last);
                    fields.add(o);
                    this.froms.put(last, fields);
                }
            }
        }
    }

    public WhereBuilder where() {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        return this.whereBuilder;
    }

    public WhereBuilder where(WhereItem item) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(item);
        return this.whereBuilder;
    }

    public WhereBuilder where(WhereBuilder whereBuilder) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(whereBuilder);
        return this.whereBuilder;
    }

    public WhereBuilder where(Object field, Object value) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(field, value);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table, Object field, Object value) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(table, field, value);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table1, Object field1, Class table2, Object field2) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(table1, field1, table2, field2);
        return this.whereBuilder;
    }

    public WhereBuilder where(Object field, SymbolType symbol, Object value) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(field, symbol, value);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table, Object field, SymbolType symbol, Object value) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(table, field, symbol, value);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table1, Object field1, SymbolType symbol, Class table2, Object field2) {
        this.whereBuilder = new WhereBuilder(this, WhereType.SELECT);
        this.whereBuilder.and(table1, field1, symbol, table2, field2);
        return this.whereBuilder;
    }

    public JoinBuilder leftJoin(Class table) {
        JoinBuilder joinBuilder = new JoinBuilder(table);
        joinBuilder.setJoinType(JoinType.LEFT_JOIN);
        this.joinBuilders.add(joinBuilder);
        return joinBuilder;
    }

    public JoinBuilder innerJoin(Class table) {
        JoinBuilder joinBuilder = new JoinBuilder(table);
        joinBuilder.setJoinType(JoinType.INNER_JOIN);
        this.joinBuilders.add(joinBuilder);
        return joinBuilder;
    }

    public JoinBuilder fullJoin(Class table) {
        JoinBuilder joinBuilder = new JoinBuilder(table);
        joinBuilder.setJoinType(JoinType.FULL_JOIN);
        this.joinBuilders.add(joinBuilder);
        return joinBuilder;
    }

    public SelectBuilder limit(long start, long limit) {
        this.restrict.add(new LimitBuilder(start, limit));
        return this;
    }

    public SelectBuilder limit(LimitBuilder limitBuilder) {
        this.restrict.add(limitBuilder);
        return this;
    }

    public SelectBuilder order(Object field, OrderType orderType) {
        this.restrict.add(new OrderBuilder(field, orderType));
        return this;
    }

    public SelectBuilder order(Class table, Object field, OrderType orderType) {
        this.restrict.add(new OrderBuilder(table, field, orderType));
        return this;
    }

    public SelectBuilder order(OrderBuilder orderType) {
        this.restrict.add(orderType);
        return this;
    }

    public SelectBuilder group(Object field) {
        this.restrict.add(new GroupBuilder(field));
        return this;
    }

    public SelectBuilder group(Class table, Object field) {
        this.restrict.add(new GroupBuilder(table, field));
        return this;
    }

    public SelectBuilder group(GroupBuilder groupBuilder) {
        this.restrict.add(groupBuilder);
        return this;
    }

    public SelectBuilder having(HavingBuilder havingBuilder) {
        this.restrict.add(havingBuilder);
        return this;
    }

    public SelectBuilder having(FunType funType, Object field, SymbolType symbol, Object value) {
        this.restrict.add(new HavingBuilder(new FunBuilder(field, funType), symbol, value));
        return this;
    }

    public SelectBuilder having(FunType funType, Class table, Object field, SymbolType symbol, Object value) {
        this.restrict.add(new HavingBuilder(new FunBuilder(table, field, funType), symbol, value));
        return this;
    }

    public Map<Class, List<Object>> getFroms() {
        return froms;
    }

    public List<JoinBuilder> getJoinBuilders() {
        return joinBuilders;
    }

    public WhereBuilder getWhereBuilder() {
        return whereBuilder;
    }

    public List<Object> getRestrict() {
        return restrict;
    }

    @Override
    public Set<Class> getAllTables() {
        Set<Class> classes = new HashSet<>();
        Set<Map.Entry<Class, List<Object>>> set = this.froms.entrySet();
        for (Map.Entry<Class, List<Object>> entry : set) {
            classes.add(entry.getKey());
        }
        if (joinBuilders != null) {
            for (JoinBuilder joinBuilder : joinBuilders) {
                classes.add(joinBuilder.getTable());
            }
        }
        return classes;
    }
}
