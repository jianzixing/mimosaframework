package org.mimosaframework.orm.sql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectBuilder extends Builder {
    /**
     * 如果List<Object>是null或者空则查询全部字段
     * List<Object>字段列表中如果Object属于特殊类型
     * 则特殊处理，比如函数类型
     */
    private Map<Class, List<Object>> froms = new LinkedHashMap<>(1);
    private List<JoinBuilder> joinBuilders = new ArrayList<>(1);
    private WhereBuilder whereBuilder;

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
}
