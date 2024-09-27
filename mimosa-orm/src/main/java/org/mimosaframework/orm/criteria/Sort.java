package org.mimosaframework.orm.criteria;

public enum Sort {
    ASC,
    DESC;

    public boolean isAsc() {
        return ASC.equals(this);
    }

    public boolean isDesc() {
        return DESC.equals(this);
    }
}
