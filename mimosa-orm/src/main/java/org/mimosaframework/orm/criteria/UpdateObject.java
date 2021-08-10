package org.mimosaframework.orm.criteria;

import java.io.Serializable;

/**
 * 使用bean的时候如何设置null是个问题所以包装一下
 */
public class UpdateObject<T> {
    private T obj;
    private Serializable[] nullFields;

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

    public UpdateObject nulls(Serializable... fields) {
        this.nullFields = fields;
        return this;
    }

    public Serializable[] getNullFields() {
        return nullFields;
    }
}
