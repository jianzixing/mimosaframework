package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.utils.ClassUtils;

/**
 * 使用bean的时候如何设置null是个问题所以包装一下
 */
public class UpdateObject<T> {
    private T obj;
    private String[] nullFields;
    private String[] retainFields;
    private String[] excludeFields;
    private boolean full = false;

    public static <T> UpdateObject<T> wrap(T obj, Object... fields) {
        return new UpdateObject(obj).nulls(fields);
    }

    public static <T> UpdateObject<T> wrapNulls(T obj, Object... fields) {
        return new UpdateObject(obj).nulls(fields);
    }

    public static <T> UpdateObject<T> wrapRetains(T obj, Object... fields) {
        return new UpdateObject(obj).retains(fields);
    }

    public static <T> UpdateObject<T> wrapExcludes(T obj, Object... fields) {
        return new UpdateObject(obj).excludes(fields);
    }

    public static <T> UpdateObject<T> wrapFull(T obj) {
        return new UpdateObject(obj).full();
    }

    public UpdateObject() {
    }

    public UpdateObject(T obj) {
        this.obj = obj;
    }

    public void set(T obj) {
        this.obj = obj;
    }

    public T get() {
        return this.obj;
    }

    public boolean isFull() {
        return full;
    }

    public UpdateObject nulls(Object... fields) {
        this.nullFields = ClassUtils.values(fields);
        return this;
    }

    public UpdateObject retains(Object... fields) {
        this.retainFields = ClassUtils.values(fields);
        return this;
    }

    public UpdateObject excludes(Object... fields) {
        this.excludeFields = ClassUtils.values(fields);
        return this;
    }

    public UpdateObject retains(boolean full) {
        this.full = full;
        return this;
    }

    public UpdateObject full() {
        full(true);
        return this;
    }

    public UpdateObject full(boolean full) {
        this.full = full;
        return this;
    }

    public String[] getNullFields() {
        return nullFields;
    }

    public String[] getRetainFields() {
        return retainFields;
    }

    public String[] getExcludeFields() {
        return excludeFields;
    }
}
