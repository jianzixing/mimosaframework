package org.mimosaframework.orm.criteria;

import java.io.Serializable;

/**
 * 使用bean的时候如何设置null是个问题所以包装一下
 */
public class UpdateObject<T> {
    private T obj;
    private Serializable[] nullFields;
    private Serializable[] retainFields;
    private Serializable[] excludeFields;
    private boolean full = false;

    public static <T> UpdateObject<T> wrap(T obj, Serializable... fields) {
        return new UpdateObject(obj).nulls(fields);
    }

    public static <T> UpdateObject<T> wrapNulls(T obj, Serializable... fields) {
        return new UpdateObject(obj).nulls(fields);
    }

    public static <T> UpdateObject<T> wrapRetains(T obj, Serializable... fields) {
        return new UpdateObject(obj).retains(fields);
    }

    public static <T> UpdateObject<T> wrapExcludes(T obj, Serializable... fields) {
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

    public UpdateObject nulls(Serializable... fields) {
        this.nullFields = fields;
        return this;
    }

    public UpdateObject retains(Serializable... fields) {
        this.retainFields = fields;
        return this;
    }

    public UpdateObject excludes(Serializable... fields) {
        this.excludeFields = fields;
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

    public Serializable[] getNullFields() {
        return nullFields;
    }

    public Serializable[] getRetainFields() {
        return retainFields;
    }

    public Serializable[] getExcludeFields() {
        return excludeFields;
    }
}
