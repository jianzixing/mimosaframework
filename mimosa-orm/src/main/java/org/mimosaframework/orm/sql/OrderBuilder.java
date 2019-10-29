package org.mimosaframework.orm.sql;

public class OrderBuilder {
    private Class table;
    private Object field;
    private OrderType orderType;

    public OrderBuilder(Object field, OrderType orderType) {
        this.field = field;
        this.orderType = orderType;
    }

    public OrderBuilder(Class table, Object field, OrderType orderType) {
        this.table = table;
        this.field = field;
        this.orderType = orderType;
    }

    public Class getTable() {
        return table;
    }

    public Object getField() {
        return field;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
