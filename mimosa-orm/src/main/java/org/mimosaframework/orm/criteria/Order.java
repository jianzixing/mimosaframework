package org.mimosaframework.orm.criteria;

public class Order {
    private boolean isAsc;
    private Object field;
    private Class orderTableClass;

    /**
     * 在进行纯数字的字符串对比时转换成数字对比
     * 比如  12300  118000
     * 字符串对比排序是  118000 < 12300
     * 数字对比排除是    118000 > 12300
     */
    private boolean numberCompareByString = true;

    public Order() {
    }

    public Order(boolean isAsc, Object field) {
        this.isAsc = isAsc;
        this.field = field;
    }

    public Order(Class tableClass, Object field, boolean isAsc) {
        this.isAsc = isAsc;
        this.field = field;
        this.orderTableClass = tableClass;
    }

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public Class getOrderTableClass() {
        return orderTableClass;
    }

    public void setOrderTableClass(Class orderTableClass) {
        this.orderTableClass = orderTableClass;
    }

    public boolean isNumberCompareByString() {
        return numberCompareByString;
    }

    public void setNumberCompareByString(boolean numberCompareByString) {
        this.numberCompareByString = numberCompareByString;
    }
}
