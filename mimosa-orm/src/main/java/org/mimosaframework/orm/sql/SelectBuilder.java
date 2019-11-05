package org.mimosaframework.orm.sql;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.SQLAutonomously;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import java.util.*;

public class SelectBuilder extends Builder {
    /**
     * 如果List<Object>是null或者空则查询全部字段
     * List<Object>字段列表中如果Object属于特殊类型
     * 则特殊处理，比如函数类型 FunBuilder , FieldBuilder
     */
    private List<FromBuilder> froms = new ArrayList<>(1);
    private List<JoinBuilder> joinBuilders = new ArrayList<>(1);
    private WhereBuilder whereBuilder;
    private List<Object> restrict = new ArrayList<>(1);

    public SelectBuilder(FromBuilder... froms) {
        if (froms != null) {
            for (FromBuilder fb : froms) {
                this.froms.add(fb);
            }
        } else {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    SelectBuilder.class, "empty_from_builder"));
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
        JoinBuilder joinBuilder = new JoinBuilder(this, table);
        joinBuilder.setJoinType(JoinType.LEFT_JOIN);
        this.joinBuilders.add(joinBuilder);
        return joinBuilder;
    }

    public JoinBuilder innerJoin(Class table) {
        JoinBuilder joinBuilder = new JoinBuilder(this, table);
        joinBuilder.setJoinType(JoinType.INNER_JOIN);
        this.joinBuilders.add(joinBuilder);
        return joinBuilder;
    }

    // public JoinBuilder fullJoin(Class table) {
    //     JoinBuilder joinBuilder = new JoinBuilder(table);
    //     joinBuilder.setJoinType(JoinType.FULL_JOIN);
    //     this.joinBuilders.add(joinBuilder);
    //     return joinBuilder;
    // }

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

    public SelectBuilder group(Class table, Object field) {
        List<Object> fields = new ArrayList<>();
        fields.add(field);
        this.restrict.add(new GroupBuilder(table, fields));
        return this;
    }

    public SelectBuilder group(Class table, List<Object> fields) {
        this.restrict.add(new GroupBuilder(table, fields));
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

    public SelectBuilder having(FunType funType, Class table, Object field, SymbolType symbol, Object value) {
        this.restrict.add(new HavingBuilder(new FunBuilder(table, field, funType), symbol, value));
        return this;
    }

    public List<FromBuilder> getFroms() {
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
        for (FromBuilder fromBuilder : this.froms) {
            if (fromBuilder.getTable() != SelectBuilder.class) {
                classes.add(fromBuilder.getTable());
            }
        }
        if (joinBuilders != null) {
            for (JoinBuilder joinBuilder : joinBuilders) {
                classes.add(joinBuilder.getTable());
            }
        }
        return classes;
    }

    public List<Class> getFromTables() {
        List<Class> classes = new ArrayList<>();
        for (FromBuilder fromBuilder : this.froms) {
            if (fromBuilder.getTable() != SelectBuilder.class) {
                classes.add(fromBuilder.getTable());
            }
        }
        return classes;
    }

    @Override
    public SQLAutonomously autonomously() {
        return SQLAutonomously.newInstance(this);
    }

    public SelectBuilder clone() {
        SelectBuilder clone = new SelectBuilder();
        if (froms != null) {
            clone.froms = new ArrayList<>();
            for (FromBuilder fromBuilder : froms) {
                clone.froms.add(fromBuilder.clone());
            }
        }
        if (joinBuilders != null) clone.joinBuilders = new ArrayList<>(joinBuilders);
        if (whereBuilder != null) {
            clone.whereBuilder = whereBuilder.clone(this);
        }
        if (restrict != null) clone.restrict = new ArrayList<>(restrict);
        return clone;
    }

    public SelectBuilder fromCount() {
        SelectBuilder builder = this.clone();
        builder.froms = new ArrayList<>();
        if (this.froms != null) {
            for (FromBuilder fromBuilder : this.froms) {
                builder.froms.add(FromBuilder.builder(fromBuilder.getTable()));
            }
        }
        builder.froms.add(FromBuilder.builder(SelectBuilder.class, FunBuilder.builder("1", FunType.COUNT)));
        return builder;
    }
}
