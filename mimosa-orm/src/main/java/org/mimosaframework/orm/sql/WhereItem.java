package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.criteria.CriteriaLogic;

public class WhereItem {
    private Class tableLeft;
    private Object field;
    private SymbolType symbol = SymbolType.EQ;
    private Class tableRight;

    /**
     * 如果tableRight存在则value肯定是字段
     * 如果tableRight不存在则value可能是值
     * 如果value类型是SelectBuilder则属于子查询
     */
    private Object value;
    private CriteriaLogic logic = CriteriaLogic.AND;

    public WhereItem() {
    }

    public WhereItem(CriteriaLogic logic) {
        this.logic = logic;
    }

    public void set(Object field, Object value) {
        this.field = field;
        this.value = value;
    }

    public void set(Class table, Object field, Object value) {
        this.tableLeft = table;
        this.field = field;
        this.value = value;
    }

    public void set(Class table1, Object field1, Class table2, Object field2) {
        this.tableLeft = table1;
        this.field = field1;
        this.tableRight = table2;
        this.value = field2;
    }

    public void set(Object field, SymbolType symbol, Object value) {
        this.field = field;
        this.symbol = symbol;
        this.value = value;
    }

    public void set(Class table, Object field, SymbolType symbol, Object value) {
        this.tableLeft = table;
        this.field = field;
        this.symbol = symbol;
        this.value = value;
    }

    public void set(Class table1, Object field1, SymbolType symbol, Class table2, Object field2) {
        this.tableLeft = table1;
        this.field = field1;
        this.symbol = symbol;
        this.tableRight = table2;
        this.value = field2;
    }

    public void setLogic(CriteriaLogic logic) {
        this.logic = logic;
    }

    public Class getTableLeft() {
        return tableLeft;
    }

    public Object getField() {
        return field;
    }

    public SymbolType getSymbol() {
        return symbol;
    }

    public Class getTableRight() {
        return tableRight;
    }

    public Object getValue() {
        return value;
    }

    public CriteriaLogic getLogic() {
        return logic;
    }
}
