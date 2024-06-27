package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.utils.ClassUtils;

public class OrderBy {
    private boolean isAsc;
    private String field;

    public OrderBy() {
    }

    public OrderBy(String field, boolean isAsc) {
        this.isAsc = isAsc;
        this.field = field;
    }

    public OrderBy(Object field, boolean isAsc) {
        this.isAsc = isAsc;
        this.field = ClassUtils.value(field);
    }

    public <T> OrderBy(FieldFunction<T> field, boolean isAsc) {
        this.isAsc = isAsc;
        this.field = ClassUtils.value(field);
    }

    public OrderBy(String field, Sort sort) {
        this.isAsc = sort.isAsc();
        this.field = field;
    }

    public OrderBy(Object field, Sort sort) {
        this.isAsc = sort.isAsc();
        this.field = ClassUtils.value(field);
    }

    public <T> OrderBy(FieldFunction<T> field, Sort sort) {
        this.isAsc = sort.isAsc();
        this.field = ClassUtils.value(field);
    }

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
