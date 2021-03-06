package org.mimosaframework.orm.criteria;

public class OnField {
    /**
     * 子表的字段名称
     */
    private Object key;

    /**
     * 主表的字段名称
     */
    private Object value;
    private String symbol;

    public OnField(Object self, Object main, String symbol) {
        this.key = main;
        this.value = self;
        this.symbol = symbol;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
