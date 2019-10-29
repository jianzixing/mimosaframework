package org.mimosaframework.orm.sql;

public class JoinBuilder {
    private SelectBuilder selectBuilder;

    private JoinType joinType;
    private WhereBuilder whereBuilder;
    private Class table;

    public JoinBuilder(Class table) {
        this.table = table;
    }

    public JoinBuilder(SelectBuilder selectBuilder, Class table) {
        this.selectBuilder = selectBuilder;
        this.table = table;
    }

    public SelectBuilder selectBuilder() {
        return this.selectBuilder;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public WhereBuilder where() {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        return this.whereBuilder;
    }

    public WhereBuilder where(WhereItem item) {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        this.whereBuilder.and(item);
        return this.whereBuilder;
    }

    public WhereBuilder where(WhereBuilder whereBuilder) {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        this.whereBuilder.and(whereBuilder);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table, Object field, Object value) {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        this.whereBuilder.and(table, field, value);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table1, Object field1, Class table2, Object field2) {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        this.whereBuilder.and(table1, field1, table2, field2);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table, Object field, SymbolType symbol, Object value) {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        this.whereBuilder.and(table, field, symbol, value);
        return this.whereBuilder;
    }

    public WhereBuilder where(Class table1, Object field1, SymbolType symbol, Class table2, Object field2) {
        this.whereBuilder = new WhereBuilder(this.selectBuilder, WhereType.JOIN);
        this.whereBuilder.and(table1, field1, symbol, table2, field2);
        return this.whereBuilder;
    }

    public WhereBuilder getWhereBuilder() {
        return whereBuilder;
    }

    public Class getTable() {
        return table;
    }
}
