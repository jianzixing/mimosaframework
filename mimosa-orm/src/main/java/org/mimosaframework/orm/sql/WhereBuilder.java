package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.criteria.CriteriaLogic;

import java.util.ArrayList;
import java.util.List;

public class WhereBuilder {
    private SelectBuilder selectBuilder;
    private WhereType whereType;
    /**
     * 如果是WhereItem这就是简单的条件
     * 如果是WhereBuilder则就是带括号的条件(...)
     */
    private List<Object> whereItems = new ArrayList<>();
    private CriteriaLogic logic;

    public WhereBuilder() {
    }

    public WhereBuilder(WhereType whereType) {
        this.whereType = whereType;
    }

    public WhereBuilder(SelectBuilder selectBuilder, WhereType whereType) {
        this.selectBuilder = selectBuilder;
    }

    public WhereBuilder and(WhereItem item) {
        whereItems.add(item);
        return this;
    }

    public WhereBuilder and(WhereBuilder whereBuilder) {
        whereBuilder.logic = CriteriaLogic.AND;
        whereItems.add(whereBuilder);
        return this;
    }

    public WhereBuilder and(Object field, Object value) {
        WhereItem item = new WhereItem();
        item.set(field, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder and(Class table, Object field, Object value) {
        WhereItem item = new WhereItem();
        item.set(table, field, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder and(Class table1, Object field1, Class table2, Object field2) {
        WhereItem item = new WhereItem();
        item.set(table1, field1, table2, field2);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder and(Object field, SymbolType symbol, Object value) {
        WhereItem item = new WhereItem();
        item.set(field, symbol, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder and(Class table, Object field, SymbolType symbol, Object value) {
        WhereItem item = new WhereItem();
        item.set(table, field, symbol, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder and(Class table1, Object field1, SymbolType symbol, Class table2, Object field2) {
        WhereItem item = new WhereItem();
        item.set(table1, field1, symbol, table2, field2);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(WhereItem item) {
        item.setLogic(CriteriaLogic.OR);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(WhereBuilder whereBuilder) {
        whereBuilder.logic = CriteriaLogic.OR;
        whereItems.add(whereBuilder);
        return this;
    }

    public WhereBuilder or(Object field, Object value) {
        WhereItem item = new WhereItem();
        item.setLogic(CriteriaLogic.OR);
        item.set(field, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(Class table, Object field, Object value) {
        WhereItem item = new WhereItem();
        item.setLogic(CriteriaLogic.OR);
        item.set(table, field, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(Class table1, Object field1, Class table2, Object field2) {
        WhereItem item = new WhereItem();
        item.setLogic(CriteriaLogic.OR);
        item.set(table1, field1, table2, field2);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(Object field, SymbolType symbol, Object value) {
        WhereItem item = new WhereItem();
        item.setLogic(CriteriaLogic.OR);
        item.set(field, symbol, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(Class table, Object field, SymbolType symbol, Object value) {
        WhereItem item = new WhereItem();
        item.setLogic(CriteriaLogic.OR);
        item.set(table, field, symbol, value);
        whereItems.add(item);
        return this;
    }

    public WhereBuilder or(Class table1, Object field1, SymbolType symbol, Class table2, Object field2) {
        WhereItem item = new WhereItem();
        item.setLogic(CriteriaLogic.OR);
        item.set(table1, field1, symbol, table2, field2);
        whereItems.add(item);
        return this;
    }

    public SelectBuilder getSelectBuilder() {
        return selectBuilder;
    }

    public WhereType getWhereType() {
        return whereType;
    }

    public List<Object> getWhereItems() {
        return whereItems;
    }

    public CriteriaLogic getLogic() {
        return logic;
    }
}
