package org.mimosaframework.orm.criteria;

public class OrderBy {
    private boolean isAsc;
    private Object field;

    public OrderBy() {
    }

    public OrderBy(boolean isAsc, Object field) {
        this.isAsc = isAsc;
        this.field = field;
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
}
